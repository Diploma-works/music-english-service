from typing import Optional, List

from pydantic import BaseModel


class PlaylistProcessRequest(BaseModel):
    username: str
    playlist_url: str


class DownloadTrackRequest(BaseModel):
    username: str
    track_id: str

class TrackReadDto(BaseModel):
    id: str
    authors: List[str]
    title: str
    content_warning: Optional[str] = None
    lyrics_count: int

class TrackReadDtoWithLyrics(BaseModel):
    id: str
    authors: List[str]
    sync_lyrics: str
    title: str
    content_warning: Optional[str] = None

class PlaylistReadDto(BaseModel):
    id: str
    title: str
    tracks: List[TrackReadDto]


class PlaylistReadList(BaseModel):
    tracks: List[TrackReadDto]


class DownloadTrackResponse(BaseModel):
    successful: bool
    reason: str


class GetTrackReadRequest(BaseModel):
    playlist_id: str
    track_id: str
