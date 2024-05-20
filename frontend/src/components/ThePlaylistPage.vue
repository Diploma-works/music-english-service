<template>
    <div class="page-content__container">
        <div class="user-info">
            <div class="user-info">
                <div class="user-photo"></div>
                <div class="user-info__text">
                    <p class="user-info__text-name">{{ playlistInfo.title }}</p>
                </div>
            </div>
            <div class="user-buttons">
                <button @click="pressAddPlaylist">Добавить плейлист</button>
                <button @click="pressList">Все плейлисты</button>
            </div>
        </div>
        <div class="table">
            <div class="line" v-for="(track, index) in playlistInfo.tracks" :key="track"
                @click="openTrackPage(track.title)" :class="index % 2 === 0 ? 'grey' : 'white'">
                <div class="line-name">{{ track.title }}</div>
                <div class="line-author">{{ track.authors[0] }}</div>
                <div class="line-author">{{ track.year }}</div>
            </div>
        </div>
    </div>
</template>

<script>

export default {
    data() {
        return {
            // playlistInfo: {
            //     name: 'Playlist',
            //     storage: [
            //         {
            //             name: 'Enjoy the Silence',
            //             author: 'Depeche Mode',
            //             year: '1990'
            //         }, {
            //             name: 'Enjoy the Silence',
            //             author: 'Depeche Mode',
            //             year: '1990'
            //         }, {
            //             name: 'Enjoy the Silence',
            //             author: 'Depeche Mode',
            //             year: '1990'
            //         }, {
            //             name: 'Enjoy the Silence',
            //             author: 'Depeche Mode',
            //             year: '1990'
            //         }
            //     ]
            // },
            playlistInfo: {
                "id": "254306981:1017",
                "title": "debug matveezy",
                "progress": 30,
                "tracks": [
                    {
                        "id": "112047068:25129938",
                        "authors": [
                            "FENDIGLOCK"
                        ],
                        "title": "Памятник",
                        "content_warning": "explicit",
                        "lyrics_count": 35,
                        "year": 2000
                    },
                    {
                        "id": "4214:4241617",
                        "authors": [
                            "50 Cent"
                        ],
                        "title": "Just A Lil Bit",
                        "content_warning": "explicit",
                        "lyrics_count": 74,
                        "year": 2000
                    }
                ]
            }
        }
    },
    props: {
        playlistStorage: Object
    },
    methods: {
        pressList() {
            this.$emit('openList');
        },
        pressAddPlaylist() {
            this.$emit('openLink');
        },
        openTrackPage(name) {
            this.$emit('openTrack', name);
        }
    },
    mounted() {
        console.log(this.playlistStorage);
        this.playlistInfo = this.playlistStorage; // сохранение информации в локальное хранилище компонента
    }
}
</script>