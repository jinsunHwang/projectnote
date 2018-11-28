var express = require("express");
var formidable = require("formidable");
var db = require("../db");
var router = express.Router();

var LOADING_SIZE = 20;
// var DEFAULT_USER_LATITUDE = 37.566229;
// var DEFAULT_USER_LONGITUDE = 126.977689;

//note/info
router.post("/info", function(req, res, next) {
  if (!req.body.member_seq) {
    return res.sendStatus(400);
  }

  var member_seq = req.body.member_seq;
  var title = req.body.title;
  var content = req.body.content;

  var sql_insert =
    "insert into note_info (member_seq,title,content) " + "values(?,?,?); ";

  console.log(sql_insert);

  var params = [member_seq, title, content];

  db.get().query(sql_insert, params, function(err, result) {
    console.log(result.insertId);
    res.status(200).send("" + result.insertId);
  });
});

//note/info/image
router.post("/info/image", function(req, res) {
  var form = new formidable.IncomingForm();

  form.on("fileBegin", function(name, file) {
    file.path = "./public/img/" + file.name;
  });

  form.parse(req, function(err, fields, files) {
    var sql_insert =
      "insert into note_info_image (info_seq, filename) values (?, ?);";

    db.get().query(sql_insert, [fields.info_seq, files.file.name], function(
      err,
      rows
    ) {
      res.sendStatus(200);
    });
    console.log("sql_insert : " + sql_insert);
  });
});

//note/info/:seq
router.get("/info/:seq", function(req, res, next) {
  var seq = req.params.seq;
  var member_seq = req.query.member_seq;

  var sql =
    "select a.*, " +
    // "  '0' as user_distance_meter, " +
    "  if( exists(select * from note_keep where member_seq = ? and a.seq = info_seq), 'true', 'false') as is_keep, " +
    "  (select filename from note_info_image where info_seq = a.seq order by seq limit 1) as image_filename " +
    "from note_info as a " +
    "where seq = ? ; ";
  console.log("sql : " + sql);

  db.get().query(sql, [member_seq, seq], function(err, rows) {
    if (err) return res.sendStatus(400);

    console.log("rows : " + JSON.stringify(rows));
    res.json(rows[0]);
  });
});

//note/list
router.get("/list", function(req, res, next) {
  var member_seq = req.query.member_seq;
  var order_type = req.query.order_type;
  var current_page = req.query.current_page || 0;

  if (!member_seq) {
    return res.sendStatus(400);
  }

  var order_add = "";

  // if (order_type) {
  //   order_add = order_type + " desc, user_distance_meter";
  // } else {
  //   order_add = "user_distance_meter";
  // }

  var start_page = current_page * LOADING_SIZE;

  var sql =
    "select a.*, " +
    "  if( exists(select * from note_keep where member_seq = ? and info_seq = a.seq), 'true', 'false') as is_keep, " +
    "  (select filename from note_info_image where info_seq = a.seq) as image_filename " +
    "from note_info as a " +
    "order by reg_date desc " +
    "limit ? , ? ; ";
  // var sql = "select * from note_info";
  console.log("sql : " + sql);
  console.log("order_add : " + order_add);

  var params = [member_seq, start_page, LOADING_SIZE];

  db.get().query(sql, params, function(err, rows) {
    if (err) return res.sendStatus(400);

    console.log("rows : " + JSON.stringify(rows));
    res.status(200).json(rows);
  });
});

module.exports = router;
