// fe_management/src/api.js
//
// - Development (npm start):  gọi thẳng http://localhost:8080
// - Docker (nginx):           gọi /api/... → nginx proxy → api-gateway
//   (baseURL = "" nghĩa là gọi relative URL, nginx sẽ forward)

import axios from 'axios';

const BASE_URL = process.env.REACT_APP_API_URL || '';

const API = axios.create({
  baseURL: BASE_URL,
  headers: { 'Content-Type': 'application/json' },
});

export default API;