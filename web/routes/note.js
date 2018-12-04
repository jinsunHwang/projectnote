var express = require("express");
var formidable = require("formidable");
var db = require("../db");
var router = express.Router();

var LOADING_SIZE = 20;

router.post("/info", function(req, res, next) {
  if (!req.body.member_seq) {
    return res.sendStatus(400);
  }

  var info_seq = req.body.seq;
  var member_seq = req.body.member_seq;
  var title = req.body.title;
  var content = req.body.content;

  var sql_count =
    "select count(*) as cnt " + "from note_info " + "where seq = ?;";
  console.log("sql_count : " + sql_count);

  var sql_insert =
    "insert into note_info (member_seq,title,content) " + "values(?,?,?); ";
  var sql_update =
    "update note_info set title = ? , content = ? where seq = ?;";
  var sql_select = "select * from note_info where seq = ?;";

  console.log(sql_insert);

  var params = [member_seq, title, content];

  db.get().query(sql_count, info_seq, function(err, rows) {
    console.log("logloglog---------- :" + rows[0].cnt);
    if (rows[0].cnt > 0) {
      console.log("sql_update : " + sql_update);

      db.get().query(sql_update, [title, content, info_seq], function(
        err,
        result
      ) {
        if (err) return res.sendStatus(400);
        console.log(result);

        db.get().query(sql_select, info_seq, function(err, rows) {
          if (err) return res.sendStatus(400);

          res.status(200).send("" + rows[0].seq);
        });
      });
    } else {
      console.log("sql_insert : " + sql_insert);

      db.get().query(sql_insert, params, function(err, result) {
        if (err) return res.sendStatus(400);

        console.log(result.insertId);
        res.status(200).send("" + result.insertId);
      });
    }
  });
});

router.post("/info/image", function(req, res) {
  var form = new formidable.IncomingForm();

  form.on("fileBegin", function(name, file) {
    file.path = "./public/img/" + file.name;
  });

  form.parse(req, function(err, fields, files) {
    var sql_count =
      "select count(*) as cnt from note_info_image where info_seq = ?;";

    var sql_insert =
      "insert into note_info_image (info_seq, filename) values (?, ?);";
    var sql_update =
      "update note_info_image set filename = ? where info_seq = ?;";
    var sql_select = "select * from note_info_image where info_seq ?;";

    db.get().query(sql_count, fields.info_seq, function(err, rows) {
      console.log("log------:" + rows[0].cnt);

      if (rows[0].cnt > 0) {
        console.log("sql_update :" + sql_update);

        db.get().query(sql_update, [files.file.name, fields.info_seq], function(
          err,
          result
        ) {
          if (err) return res.sendStatus(400);
          console.log(result);

          db.get().query(sql_select, [fields.info_seq], function(err, rows) {
            if (err) return res.sendStatus(400);

            res.status(200);
          });
        });
      } else {
        db.get().query(sql_insert, [fields.info_seq, files.file.name], function(
          err,
          rows
        ) {
          res.sendStatus(200);
        });
        console.log("sql_insert : " + sql_insert);
      }
    });
  });
});

//note/info/:seq
router.get("/info/:seq", function(req, res, next) {
  var seq = req.params.seq;
  var member_seq = req.query.member_seq;

  var sql =
    "select a.*, " +
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

//note/:info_seq
router.delete("/:info/:seq", function(req, res, next) {
  var seq = req.params.seq;
  console.log(seq);

  if (!seq) {
    return res.sendStatus(400);
  }

  var sql_delete = "delete from note_info where seq = ?";
  var sql_delete2 = "delete from note_info_image where info_seq = ?";

  db.get().query(sql_delete, seq, function(err, rows) {
    db.get().query(sql_delete2, seq, function(err, rows) {
      if (err) return res.sendStatus(400);
      res.sendStatus(200);
    });
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

  var start_page = current_page * LOADING_SIZE;

  var sql =
    "select a.*, " +
    "  if( exists(select * from note_keep where member_seq = ? and info_seq = a.seq), 'true', 'false') as is_keep, " +
    "  (select filename from note_info_image where info_seq = a.seq) as image_filename " +
    "from note_info as a " +
    "where member_seq = ?" +
    "order by reg_date desc ";

  console.log("sql : " + sql);
  console.log("order_add : " + order_add);

  var params = [member_seq, member_seq];

  db.get().query(sql, params, function(err, rows) {
    if (err) return res.sendStatus(400);

    console.log("rows : " + JSON.stringify(rows));
    res.status(200).json(rows);
  });
});

//note/share
router.get("/share", function(req, res, next) {
  var order_type = req.query.order_type;
  var current_page = req.query.current_page || 0;

  var order_add = "";

  var start_page = current_page * LOADING_SIZE;

  var sql =
    "select a.*, " +
    " (select filename from note_info_image where info_seq = a.seq) as image_filename " +
    "from note_info as a " +
    "where keep_cnt = 1 " +
    "order by reg_date desc ";

  console.log("sql : " + sql);
  console.log("order_add : " + order_add);

  var params = [start_page, LOADING_SIZE];

  db.get().query(sql, function(err, rows) {
    if (err) return res.sendStatus(400);

    console.log("rows : " + JSON.stringify(rows));
    res.status(200).json(rows);
  });
});

module.exports = router;
