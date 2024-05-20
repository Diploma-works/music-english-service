<template>
    <div class="page-link__container">
        <div class="form addlink-container">
            <p>Введите ссылку на плейлист</p>
            <input type="text" v-model.trim="playlistLink" v-bind:class="{ empty_input: linkIsEmpty }"
                placeholder="Ссылка">
            <button className="login-btn" @click="addPlaylist()">Добавить плейлист</button>
        </div>
    </div>
</template>

<script>

// import axios from 'axios';

export default {
    data() {
        return {
            playlistLink: '',
            linkIsEmpty: false
        }
    },
    props: {
        userInfo: Object
    },
    methods: {
        addPlaylist() {
            this.linkIsEmpty = !this.playlistLink || this.playlistLink.trim().length === 0;

            if (!this.linkIsEmpty) {
                console.log('добавить плейлист');
                // axios.post('http://localhost:8765/playlist/playlist/playlists', {
                //     "username": this.userInfo.name,
                //     "playlist_url": this.playlistLink
                // }, {
                //     headers: {
                //         'Authorization': `Bearer ${sessionStorage.getItem('token')}`,
                //         'Content-Type': 'application/json; charset=utf-8'
                //     }
                // }).then((response) => {
                //     if (response.status === 201) { // если успешно
                //         console.log(response.data);

                this.$emit('openPlaylist', { // вставить ответ от сервера (объект с инфой о плейлисте)
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
                            "lyrics_count": 35
                        },
                        {
                            "id": "4214:4241617",
                            "authors": [
                                "50 Cent"
                            ],
                            "title": "Just A Lil Bit",
                            "content_warning": "explicit",
                            "lyrics_count": 74
                        }
                    ]
                }
                );
                //     }
                // }).catch((error) => {
                //     console.log(error);
                // });
            }
        }
    }
}
</script>

<style>
.page-link__container {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
}

.addlink-container p {
    font-size: 24px;
    font-weight: 600;
    line-height: 75%;
    text-align: center;
    color: #171802;
    margin: 0 0 32px;
}
</style>