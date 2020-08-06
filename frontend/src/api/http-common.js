import axios from "axios";

export default axios.create({
    baseURL: "http://i3a407.p.ssafy.io:8081/",
    headers: {
        "Content-type": "application/json",
    }
});
