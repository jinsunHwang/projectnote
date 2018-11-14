// 별도의 파일에서 라우트 함수를 작성할 때 express.Router()사용
var express = require("express");
var formidable = require("formidable");
var db = require("../db");
var router = express.Router();

/* GET home page. */
// URL경로가 /일때 호출되는 함수 router.get()
// req HTTP 요청 객체 res HTTP 응답 객체
router.get("/:phone", function(req, res, next) {
  var phone = req.params.phone;

  var sql = "select * " + "from note_member " + "where phone = ? limit 1;";
  console.log("sql : " + sql);

  db.get().query(sql, phone, function(err, rows) {
    console.log("rows : " + JSON.stringify(rows));
    console.log("row.length : " + rows.length);
    if (rows.length > 0) {
      res.status(200).json(rows[0]);
    } else {
      res.sendStatus(400);
    }
  });
});

// member/phone
router.post("/phone", function(req, res) {
  var phone = req.body.phone;

  var sql_count =
    "select count(*) as cnt " + "from note_member " + "where phone = ?;";
  console.log("sql_count : " + sql_count);

  var sql_insert = "insert into note_member (phone) values(?);";

  db.get().query(sql_count, phone, function(err, rows) {
    console.log(rows);
    console.log(rows[0].cnt);

    if (rows[0].cnt > 0) {
      return res.sendStatus(400);
    }

    db.get().query(sql_insert, phone, function(err, result) {
      console.log(err);
      if (err) return res.sendStatus(400);
      res.status(200).send("" + result.insertId);
    });
  });
});
// 별도파일에서 작성한 함수를 사용할 수 있도록 moudle.exports를 추가
module.exports = router;
