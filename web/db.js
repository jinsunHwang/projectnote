// 외부 모듈을 해당파일에서 사용하고 싶다면 require() 함수를 호출해야됨
// mysql 모듈을 로딩하는 코드
var mysql = require("mysql");

var pool;

// exports.connect 커넥션 풀 생성하는함수
// createPool() 함수를 호출해서 커넥션 풀을 생성함
// exports는 외부에서 이 함수들을 사용할 수 있게 해주는 자바 스크립트 키워드
exports.connect = function() {
  pool = mysql.createPool({
    connectionLimit: 100, //  커넥션 풀 최대 개수(사용량에 따라 변경할 필요 있다.)
    host: "localhost",
    user: "root",
    password: "jinsun",
    database: "note"
  });
};

// get 함수는 커넥션 풀을 반환하는 함수이다.
exports.get = function() {
  return pool;
};
