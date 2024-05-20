from fastapi import FastAPI
from yandex_music import Client, Track, Playlist, TrackId
import logging
import re
import os
import config
from model import PlaylistProcessRequest, TrackReadDto, PlaylistReadList, DownloadTrackResponse, DownloadTrackRequest, \
    PlaylistReadDto, GetTrackReadRequest, TrackReadDtoWithLyrics
import boto3
from botocore.exceptions import ClientError

app = FastAPI()
client = Client('***').init()
s3_client = boto3.client('s3',
                         region_name='ru-1',
                         endpoint_url=config.USER_STORAGE_URL,
                         aws_access_key_id=config.AWS_ACCESS_KEY,
                         aws_secret_access_key=config.AWS_SECRET_KEY
                         )

@app.post("/playlists")
async def hande_playlist(request: PlaylistProcessRequest) -> PlaylistReadDto:
    tracks = []
    playlist_id = extract_playlist_id_from_url(request.playlist_url)
    playlist = client.users_playlists(playlist_id)
    upload_track_covers(playlist)
    write_tracks_info_to_file_and_upload(playlist, request.username)
    return PlaylistReadDto(id=playlist.playlist_id, title=playlist.title, tracks=track_list_read(playlist))


@app.get("/playlists/{playlist_id}")
async def get_playlist_by_id(playlist_id: str) -> PlaylistReadDto:
    playlist = client.users_playlists(playlist_id.split(':')[1])
    return PlaylistReadDto(id=playlist.playlist_id, title=playlist.title, tracks=track_list_read(playlist))


@app.post("/tracks")
def download_track_by_id(request: DownloadTrackRequest) -> DownloadTrackResponse:
    filename = f"{request.track_id}.mp3"
    client.tracks(request.track_id)[0].download(filename)
    upload_file_to_s3(filename, request.username, filename, True)
    return DownloadTrackResponse(successful=True, reason="")


@app.get("/tracks/{track_id}")
def get_track_with_lyrics_by_id(track_id: str) -> TrackReadDtoWithLyrics:
    track = client.tracks(track_id)[0]
    authors = [artist.name for artist in track.artists]
    return TrackReadDtoWithLyrics(
        id=track.track_id,
        title=track.title,
        authors=authors,
        sync_lyrics=track.get_lyrics('LRC').fetch_lyrics(),
        content_warning=track.content_warning
    )


def write_tracks_info_to_file_and_upload(playlist: Playlist, username: str):
    with open(username, 'w') as file:
        for i, track in enumerate(playlist.tracks):
            if track.track.lyrics_info.has_available_sync_lyrics:
                full_track = track.track
                authors = [artist.name for artist in full_track.artists]
                track_id = full_track.track_id
                artists = ', '.join(authors)
                track_title = full_track.title

                file.write(f"{i}. ")
                file.write(f"Track id: {track_id}. ")
                file.write(f"Title: {track_title}. ")
                file.write(f"Author: {artists}")
                file.write("\n")
        file.seek(0)
        upload_file_to_s3(file.name, 'playlists-info', f"{username}-{playlist.playlist_id}-tracks.txt", False)


def track_list_read(playlist: Playlist):
    track_list = []
    for i, track in enumerate(playlist.tracks):
        if track.track.lyrics_info.has_available_sync_lyrics:
            track_read_dto = TrackReadDto(id=track.track.track_id, title=track.track.title,
                                          authors=[artist.name for artist in track.track.artists],
                                          content_warning=track.track.content_warning,
                                          lyrics_count=track.track.get_lyrics('LRC').fetch_lyrics().count('\n'))
            track_list.append(track_read_dto)
    return track_list


def upload_file_to_s3(file_path, bucket_name, object_name, exist_check: bool):
    try:
        create_bucket_if_not_exists(bucket_name, 'ru-1')
        if exist_check:
            if check_file_exists(bucket_name, object_name):
                print(f"Файл {object_name} уже существует в бакете {bucket_name}. Загрузка отменена.")
            else:
                s3_client.upload_file(file_path, bucket_name, object_name)
                os.remove(file_path)
                print(f"Файл {object_name} успешно загружен в бакет {bucket_name}.")
        else:
            s3_client.upload_file(file_path, bucket_name, object_name)
            os.remove(file_path)
            print(f"Файл {object_name} успешно загружен в бакет {bucket_name}.")
    except Exception as e:
        print(f"Ошибка при загрузке файла в S3: {e}")

def upload_track_covers(playlist: Playlist):
    try:
        create_bucket_if_not_exists('track-covers', 'ru-1')
        for i, track in enumerate(playlist.tracks):
            track.track.download_cover(f"{track.track.id}-cover.jpg")
            upload_file_to_s3(f"{track.track.id}-cover.jpg", 'track-covers', f"{track.track.id}-cover.jpg", True)
            os.remove(f"{track.track.id}-cover.jpg")
    except Exception as e:
        print(f"Ошибка при загрузке файла в S3: {e}")

def check_file_exists(bucket_name, key):
    try:
        s3_client.head_object(Bucket=bucket_name, Key=key)
        return True
    except Exception as e:
        return False


def create_bucket_if_not_exists(bucket_name, region):
    try:
        s3_client.head_bucket(Bucket=bucket_name)
    except ClientError as e:
        # Если бакет не существует, создаем его
        if e.response['Error']['Code'] == '404':
            try:
                response = s3_client.create_bucket(
                    Bucket=bucket_name,
                    CreateBucketConfiguration={
                        'LocationConstraint': region
                    }
                )
                print(f"Бакет '{bucket_name}' успешно создан.")
            except ClientError as e:
                print(f"Ошибка при создании бакета: {e}")
        else:
            print(f"Ошибка при проверке бакета: {e}")
    else:
        print(f"Бакет '{bucket_name}' уже существует.")


def extract_playlist_id_from_url(playlist_url: str) -> str:
    match = re.search(r'/playlists/(\d+)', playlist_url)
    if match:
        playlist_id = match.group(1)
        return playlist_id
    else:
        return None
