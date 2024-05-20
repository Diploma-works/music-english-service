<template>
    <div class="page-content__container">
        <div class="user-info">
            <div class="user-info">
                <div class="user-photo"></div>
                <div class="user-info__text">
                    <p class="user-info__text-name">{{ userInfo.username }}</p>
                    <div class="user-info__text-add">
                        <span>{{ userInfo.age }}</span>
                        <div class="separator"></div><span>{{ userInfo.email }}</span>
                    </div>
                </div>
            </div>
            <div class="user-buttons">
                <button @click="pressAddPlaylist">Добавить плейлист</button>
                <button @click="pressList">Все плейлисты</button>
            </div>
        </div>
        <div class="main-image__container">
            <div class="main-image"></div>
            <p>Продолжайте в том же духе!</p>
        </div>
        <div class="progress-container">
            <div class="progress-value">прогресс<div>{{ userInfo.user_summary_progress }}%</div>
            </div>
            <input class="slider-wrapper slider-progress" id="progress" type="range" min="0" max="100" disabled />
        </div>
    </div>
</template>

<script>

// import axios from 'axios';

export default {
    data() {
        return {
            shift: 12
        }
    },
    props: {
        userInfo: Object
    },
    methods: {
        pressList() {
            this.$emit('openList');
        },
        pressAddPlaylist() {
            this.$emit('openLink');
        }
    },
    mounted() {
        // PROGRESS
        const progressInput = document.getElementById('progress');
        // Установить значение из data() в атрибут value элемента input
        progressInput.value = Number(this.userInfo.user_summary_progress);

        progressInput.style.setProperty('background', `-webkit-linear-gradient(left, #DDEA4D 0%, #DDEA4D ${progressInput.value}%, #E6E7DF ${progressInput.value}%, #E6E7DF 100%)`, 'important');

        if (progressInput.value < 4) {
            this.shift = -4
        } else if (progressInput.value < 10) {
            this.shift = 0
        } else if (progressInput.value < 20) {
            this.shift = 5
        } else if (progressInput.value > 91) {
            this.shift = 20
        } else if (progressInput.value > 101) {
            this.shift = 30
        }

        let progressValue = document.querySelector('.progress-value');
        progressValue.style.marginLeft = `${progressInput.value - this.shift}%`;
    }
}
</script>

<style>
.user-info {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.user-info__text-add {
    display: flex;
    align-items: center;
}

.user-photo {
    background-image: url(../img/userphoto.svg);
    width: 168px;
    height: 168px;
    background-repeat: no-repeat;
}

.user-info__text {
    margin-left: 24px;
}

.user-info__text-name {
    font-weight: 600;
    font-size: 48px;
    color: #171802;
    font-family: 'RockStar';
    margin: 0;
    margin-bottom: 6px;
}

.separator {
    width: 3px;
    height: 28px;
    background-color: #DDEA4D;
    margin: 0 16px;
}

.user-info span {
    font-weight: 400;
    font-size: 24px;
    color: #88897A;
}

.user-buttons {
    display: flex;
    flex-direction: column;
}

.user-buttons button {
    font-weight: 500;
    font-size: 16px;
    line-height: 112%;
    text-align: center;
    color: #171802;
    font-family: 'Open Sans';
    width: 362px;
    height: 48px;
    border-radius: 24px;
}

.user-buttons button:first-child {
    margin-bottom: 8px;
    background-color: #DDEA4D;
}

.user-buttons button:last-child {
    background-color: #EDD16C;
}

.main-image {
    background-image: url(../img/main-image.svg);
    width: 100%;
    height: 257px;
    background-repeat: no-repeat;
    background-position: center;
    background-size: cover;
    border-radius: 24px;
}

.main-image__container {
    margin-top: 28px;
    margin-bottom: 28px;
}

.main-image__container p {
    font-weight: 400;
    font-size: 24px;
    color: #171802;
    text-align: center;
    margin: 16px 0 0;
    text-align: center;
}

#progress {
    width: 90%;
    margin: 0 48px;
}

.progress-container {
    width: 100%;
    border: 1px solid #E6E7DF;
    border-radius: 24px;
    padding: 32px 0;
}

.slider-wrapper {
    -webkit-appearance: none;
    background: -webkit-linear-gradient(left, #DDEA4D 0%, #DDEA4D 64%, #E6E7DF 64%, #E6E7DF 100%);
    border-radius: 24px;
    height: 28px !important;
}

.slider-wrapper:focus {
    outline: none;
}

/*webkit*/
.slider-wrapper::-webkit-slider-thumb {
    -webkit-appearance: none;
    width: 40px;
    height: 40px;
    border-radius: 40px;
    background: #F8F9F2;
    border: 8px solid #DDEA4D;
    box-shadow: 0 0 2px #00000000;
    margin-top: -13px;
}

.slider-wrapper::-webkit-slider-runnable-track {
    height: 1em;
    border: none;
    border-radius: 0.5em;
    box-shadow: none;
}

.progress-value {
    border-radius: 24px;
    width: 220px;
    height: 48px;
    background: #DDEA4D;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 16px;
    font-family: 'Open Sans';
    font-weight: 400;
    font-size: 20px;
    color: #171802;
}

.progress-value div {
    margin-left: 8px;
    margin-top: 5px;
    font-family: 'RockStar';
}
</style>