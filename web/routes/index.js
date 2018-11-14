// 별도의 파일에서 라우트 함수를 작성할 때 express.Router()사용
var express = require("express");
var router = express.Router();

/* GET home page. */
// URL경로가 /일때 호출되는 함수 router.get()
// req HTTP 요청 객체 res HTTP 응답 객체
router.get("/", function(req, res, next) {
  res.render("index", { title: "Express" });
});

// 별도파일에서 작성한 함수를 사용할 수 있도록 moudle.exports를 추가
module.exports = router;
