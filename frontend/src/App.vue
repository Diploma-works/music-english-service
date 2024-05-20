<template>
  <div v-if="loginPageVisibility" class="content"
    style="width: 100%;height: 100%; vertical-align: center; position: fixed;">
    <div v-if="loginFormVisibility" class="form">
      <p class="form-title">Вход</p>
      <input type="text" v-model.trim="userName" v-bind:class="{ empty_input: userNameIsEmpty }" placeholder="Username">
      <input type="password" v-model.trim="userPass" v-bind:class="{ empty_input: userPassIsEmpty }"
        placeholder="Пароль">
      <button className="login-btn" @click="logIn()">Войти</button>
      <div className="reg-btn" @click="logRegToggle()">Регистрация</div>
    </div>
    <div v-if="errorLoginVisibility" class="form">
      <p class="reg-title">{{ errorLoginMessage }}</p>
      <button className="login-btn" @click="tryLogIn()">Попробовать снова</button>
    </div>
    <div v-if="regFormVisibility" class="form">
      <p class="form-title">Регистрация</p>
      <input type="text" v-model.trim="projUserName" v-bind:class="{ empty_input: projUserNameIsEmpty }"
        placeholder="Username">
      <input type="email" v-model.trim="projUserEmail" v-bind:class="{ empty_input: projUserEmailIsEmpty }"
        placeholder="Email">
      <input type="number" v-model.trim="projUserAge" v-bind:class="{ empty_input: projUserAgeIsEmpty }"
        placeholder="Возраст">
      <input type="password" v-model.trim="projUserPass" v-bind:class="{ empty_input: projUserPassIsEmpty }"
        placeholder="Пароль">
      <button className="login-btn" @click="createAccount()">Регистрация</button>
      <div className="reg-btn" @click="logRegToggle()">Уже есть аккаунт? <span>Войти</span></div>
    </div>
    <div v-if="regMessageVisibility" class="form">
      <p class="reg-title">Вы зарегистрированы</p>
      <button className="login-btn" @click="closeRegMessage()">Войти</button>
    </div>
    <div v-if="errorMessageVisibility" class="form">
      <p class="reg-title">Ошибка</p>
      <button className="login-btn" @click="tryRegWindow()">Попробовать снова</button>
    </div>
  </div>
  <div v-if="mainPageVisibility">
    <div className="wrapper">
      <div class="sidenav" v-bind:class="{ sidenav_hidden: sidenavIsHidden }">
        <!-- <div className="side-menu__btn" @click="sidenavToggle()">
          <div className="icon-burger"></div>
        </div> -->
        <nav className="sidenav__block">
          <div class="sidenav__block-menu">
            <button @click="openAccount()" v-bind:class="{ sidebtn_active: pages.accountPageVisibility }">
              <div class="icon-account" v-bind:class="{ iconcontrol_active: pages.accountPageVisibility }">
              </div>Профиль
            </button>
            <button @click="openList()" v-bind:class="{ sidebtn_active: pages.listPageVisibility }">
              <div class="icon-playlists" v-bind:class="{ iconproj_active: pages.listPageVisibility }">
              </div>Плейлисты
            </button>
            <button class="playlist" @click="openPlaylist()"
              v-bind:class="{ sidebtn_active: pages.playlistPageVisibility }">
              <div class="icon-personal-area"
                v-bind:class="{ icon_personal_area_active: pages.playlistPageVisibility }">
              </div>Плейлист-1
            </button>
          </div>
          <button id="sidenav__logout" @click="logOut()" v-bind:class="{ sidebtn_active: pages.logoutVisibility }">
            <div class="icon-logout" v-bind:class="{ iconmap_active: pages.logoutVisibility }"></div>Выход
          </button>
        </nav>
      </div>
      <div id="page-content" class="page-content" v-bind:class="{ content_compressed: contentIsCompressed }">
        <TheAccountPage v-if="pages.accountPageVisibility" @openList="openList" @openLink="openLink"
          :userInfo="userInfo" />
        <ThePlaylistPage v-if="pages.playlistPageVisibility" @openList="openList" @openLink="openLink"
          @openTrack="openTrack" :playlistStorage="playlistStorage" />
        <ThePlaylistsPage v-if="pages.listPageVisibility" />
        <TheLinkPage v-if="pages.linkPageVisibility" @openPlaylist="openPlaylist" :userInfo="userInfo" />
        <TheTrackPage v-if="pages.trackPageVisibility" />
      </div>
    </div>
  </div>
</template>

<script>

import ThePlaylistPage from './components/ThePlaylistPage.vue'
import TheAccountPage from './components/TheAccountPage.vue'
import ThePlaylistsPage from './components/ThePlaylistsPage.vue'
import TheLinkPage from './components/TheLinkPage.vue'
import TheTrackPage from './components/TheTrackPage.vue'

// import axios from 'axios';

export default {
  components: {
    ThePlaylistPage,
    TheAccountPage,
    ThePlaylistsPage,
    TheLinkPage,
    TheTrackPage
  },
  data() {
    return {
      loginFormVisibility: true,
      regFormVisibility: false,
      regMessageVisibility: false,
      errorMessageVisibility: false,
      errorLoginVisibility: false,
      errorLoginMessage: 'Ошибка',

      mainPageVisibility: false, // доступ после авторизации
      loginPageVisibility: true, // страница авторизации

      userName: '',
      userPass: '',

      userNameIsEmpty: false,
      userPassIsEmpty: false,

      projUserNameIsEmpty: false,
      projUserEmailIsEmpty: false,
      projUserAgeIsEmpty: false,
      projUserPassIsEmpty: false,

      sidenavIsHidden: false,
      contentIsCompressed: false,

      pages: {
        logoutVisibility: false,
        listPageVisibility: false,
        accountPageVisibility: true,
        playlistPageVisibility: false,
        linkPageVisibility: false,
        trackPageVisibility: false
      },

      userInfo: {
        "username": "matveezy",
        "email": "matveezy@yandex.ru",
        "age": 21,
        "playlists": [
          {
            "yandexPlaylistId": "254306981:1017",
            "title": "debug matveezy",
            "playlist_progress": 0.0
          }
        ],
        "user_summary_progress": 0.0
      },

      playlistStorage: { // сохранение информации об одном плейлисте (для страницы одного плейлиста)
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
    };
  },
  methods: {
    logRegToggle() {
      this.loginFormVisibility = !this.loginFormVisibility;
      this.regFormVisibility = !this.regFormVisibility;
    },
    createAccount() {
      this.projUserNameIsEmpty = !this.projUserName || this.projUserName.trim().length === 0;
      this.projUserEmailIsEmpty = !this.projUserEmail || this.projUserEmail.trim().length === 0;
      this.projUserAgeIsEmpty = !this.projUserAge || this.projUserAge.length === 0;
      this.projUserPassIsEmpty = !this.projUserPass || this.projUserPass.trim().length === 0;

      if (!this.projUserNameIsEmpty && !this.projUserEmailIsEmpty && !this.projUserAgeIsEmpty && !this.projUserPassIsEmpty) {
        this.createAccountPost();
      }
    },
    createAccountPost() {
      console.log('регистрация')
      // axios.post('http://localhost:8765/auth/auth/register',
      //   {
      //     "username": this.projUserName,
      //     "password": this.projUserPass,
      //     "email": this.projUserEmail,
      //     "age": this.projUserAge
      //   }, {
      //   headers: {
      //     'Content-Type': 'application/json'
      //   }
      // }).then((response) => { // обработка ошибок
      //   if (response.status === 201) { // успешно 
      //     this.regMessageVisibility = true;
      //     this.projUserEmail = '';
      //     this.projUserAge = '';
      //     this.projUserName = '';
      //     this.projUserPass = '';
      //   } else if (response.status === 400) {
      //     this.errorMessageVisibility = true;
      //   } else {
      //     this.errorMessageVisibility = true;
      //   }
      // }).catch((error) => {
      //   console.log(error);
      //   this.errorMessageVisibility = true;
      // });

      this.regFormVisibility = false;

      if (this.regMessageVisibility === true) {
        // очистка полей в регистрации
        this.projUserEmail = '';
        this.projUserAge = '';
        this.projUserName = '';
        this.projUserPass = '';
      }
    },
    drawErrorLogIn() {
      this.loginFormVisibility = false;
      this.errorLoginVisibility = true;
    },
    tryLogIn() {
      this.loginFormVisibility = true;
      this.errorLoginVisibility = false;
    },
    closeRegMessage() {
      this.regMessageVisibility = false;
      this.loginFormVisibility = true;
      this.regFormVisibility = false;
    },
    tryRegWindow() {
      this.errorMessageVisibility = false;
      this.regFormVisibility = true;
    },
    logIn() {
      console.log('вход')

      this.userNameIsEmpty = !this.userName || this.userName.trim().length === 0;
      this.userPassIsEmpty = !this.userPass || this.userPass.trim().length === 0;

      if (!this.userNameIsEmpty && !this.userPassIsEmpty) {
        // axios.post('http://localhost:8765/auth/auth/authenticate', {
        //   "username": this.userName,
        //   "password": this.userPass
        // }, {
        //   headers: {
        //     'Content-Type': 'application/json; charset=utf-8'
        //   }
        // }).then((response) => {
        //   if (response.status === 201) { // если успешно
        //     sessionStorage.setItem('token', response.data.token); // сохранение токена в sessionStorage
        this.drawLogIn(); // переход в приложение
        //   }
        // }).catch((error) => {
        //   console.log(error);
        // });
      }
    },
    logOut() {
      // удалить токен текущего пользователя
      sessionStorage.removeItem('token');

      this.userName = '';
      this.userPass = '';
      this.mainPageVisibility = false;
      this.loginPageVisibility = true;

      for (let page in this.pages) {
        this.pages[page] = false;
      }

      this.pages.accountPageVisibility = true;
    },
    drawLogIn() { // получить информацию о пользователе
      console.log('получить информацию о пользователе')
      // axios.get(`http://localhost:8765/api/users/${this.userName}`,
      //   {
      //     headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}` },
      //   }).then((response) => { // обработка успешного запроса
      //     this.userInfo = response.data;

      this.mainPageVisibility = true;
      this.loginPageVisibility = false;
      // }).catch((error) => { // обработка ошибки
      //   console.log(error);
      // })
    },
    sidenavToggle() {
      this.sidenavIsHidden = !this.sidenavIsHidden;
      this.contentIsCompressed = !this.contentIsCompressed;
    },
    openPlaylist(playlistStorage) {
      this.playlistStorage = playlistStorage;
      for (let page in this.pages) {
        this.pages[page] = false;
      }
      this.pages.playlistPageVisibility = true;
    },
    openAccount() {
      for (let page in this.pages) {
        this.pages[page] = false;
      }
      this.pages.accountPageVisibility = true;
    },
    openList() {
      for (let page in this.pages) {
        this.pages[page] = false;
      }
      this.pages.listPageVisibility = true;
    },
    openLink() {
      for (let page in this.pages) {
        this.pages[page] = false;
      }
      this.pages.linkPageVisibility = true;
    },
    openTrack(name) {
      console.log(name);
      for (let page in this.pages) {
        this.pages[page] = false;
      }
      this.pages.trackPageVisibility = true;
    }
  }
}
</script>

<style>
/* authorization */

.form button {
  font-weight: 500;
  font-size: 16px;
  width: 360px;
  font-family: 'Open Sans';
}

.login-btn {
  padding: 14px 0;
  border-radius: 24px;
  color: #171802;
  background-color: #DDEA4D;
  margin-bottom: 16px;
  margin-top: 8px;
}

.login-btn:hover {
  box-shadow: 0 0 8px 0 rgba(41, 59, 95, 0.5);
}

.reg-btn {
  color: #171802;
  text-align: center;
  cursor: pointer;
}

.empty_input {
  outline: 2px solid #F4CA8D;
}

.reg-title {
  text-align: center;
  color: #171802;
  margin-bottom: 56px;
  font-size: 20px;
}

.reg-btn span {
  font-weight: 600;
  font-size: 16px;
  line-height: 112%;
  text-decoration: underline;
  text-decoration-skip-ink: none;
  color: #aebe00;
  margin-left: 4px;
}

/* структура страниц */

.page-content__container {
  background-color: #F8F9F2;
  padding: 40px 24px;
}
</style>