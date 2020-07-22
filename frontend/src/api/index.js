import axios from "axios";

const instance = axios.create({
  baseURL: process.env.VUE_APP_API_URL,
});
function checkEmail(email) {
  return instance.post("account/checkEmail", email);
}
function checkNickname(nickname) {
  return instance.post("account/checkNickname", nickname);
}

function registerUser(userData) {
  return instance.post("signup", userData);
}

export { checkEmail, checkNickname, registerUser };
