// url:
// method: GET | POST | PUT | DELETE
// header: {old_name -> new_name}[String -> LocalDateTime]("yyyy-MM-dd")<Range(20, 30)>
// param:
// body:
// template: { }
// result:

grammar Hello;
r  : '{' ID '->' ID '}';         // match keyword hello followed by an identifier
//OLD_NAME: ID;
//NEW_NAME: ID;
ID : [a-z]+ ;             // match lower-case identifiers
WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines