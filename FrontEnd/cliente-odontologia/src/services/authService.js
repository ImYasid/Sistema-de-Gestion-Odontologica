import axios from 'axios';

const API = axios.create({
  baseURL: 'http://localhost:8081',
  headers: { 'Content-Type': 'application/json' }
});

const ACCESS_KEY = 'odontologia_access_token';
const REFRESH_KEY = 'odontologia_refresh_token';

// Apply Authorization header to axios instance when we have a token
function setAxiosAuthHeader(token) {
  if (token) {
    API.defaults.headers.common['Authorization'] = `Bearer ${token}`;
  } else {
    delete API.defaults.headers.common['Authorization'];
  }
}

// Initialize header if token already present
setAxiosAuthHeader(localStorage.getItem(ACCESS_KEY));

export default {
  async login(username, password) {
    const resp = await API.post('/auth/login', { username, password });
    // backend devuelve { accessToken, refreshToken }
    const data = resp.data || {};
    const access = data.accessToken || data.access || null;
    const refresh = data.refreshToken || data.refresh || null;
    if (access) {
      localStorage.setItem(ACCESS_KEY, access);
      setAxiosAuthHeader(access);
    }
    if (refresh) {
      localStorage.setItem(REFRESH_KEY, refresh);
    }
    return { access, refresh };
  },
  logout() {
    localStorage.removeItem(ACCESS_KEY);
    localStorage.removeItem(REFRESH_KEY);
    setAxiosAuthHeader(null);
  },
  getAccessToken() {
    return localStorage.getItem(ACCESS_KEY);
  },
  getRefreshToken() {
    return localStorage.getItem(REFRESH_KEY);
  },
  isAuthenticated() {
    return !!localStorage.getItem(ACCESS_KEY);
  },
  getAuthHeader() {
    const t = localStorage.getItem(ACCESS_KEY);
    return t ? { Authorization: `Bearer ${t}` } : {};
  },
  api() { return API; }
};
