<?

/*
  $data = array( 'name' => 'Cathy', 'addr' => '9 Dark and Twisty', 'city' => 'Cardiff' );
  # the shortcut!
  $STH = $GLOBALS["DBH"]->("INSERT INTO folks (name, addr, city) value (:name, :addr, :city)");
  $STH->execute($data);


  $DBH->lastInsertId();   -> usar ao inves de getnext
 */
//jdbc:mysql://127.0.0.1:7188/construbook
//CREATE USER 'construbook'@'localhost' IDENTIFIED BY 'construbook123';
//GRANT ALL ON construbook.* TO 'construbook'@'localhost';
if (AMBIENTE == AMBIENTE_LOCAL) {
    $mysql_host = "127.0.0.1";
    $mysql_database = "construbook";
    $mysql_user = "construbook"; //@localhost
    $mysql_password = "construbook123";
    $mysql_port = "port=7188;";
}

if (AMBIENTE == AMBIENTE_HOMOLOG) {
    $mysql_host = "mysql4.000webhost.com";
    $mysql_database = "a7586325_db";
    $mysql_user = "a7586325_user";
    $mysql_password = "450SDgidelok";
}

function conectaPDO() {
    try {
        $GLOBALS["DBH"] = new PDO("mysql:charset=utf8;host=" . $GLOBALS["mysql_host"] . "; " . $GLOBALS["mysql_port"] . "dbname=" . $GLOBALS["mysql_database"], $GLOBALS["mysql_user"], $GLOBALS["mysql_password"]);
        $GLOBALS["DBH"]->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    } catch (PDOException $e) {
        logaError($e);
    }
}

function countQuery($sql, $array) {
    if (!isset($GLOBALS["DBH"])) {
        conectaPDO();
    }
    $stmt = $GLOBALS["DBH"]->prepare($sql);
    $stmt->execute($array);
    $GLOBALS["contaQueries"] = $GLOBALS["contaQueries"] + 1;
    $count = $stmt->rowCount();
    return $count;
}

function existsQuery($sql, $array) {
    if (!executaQuerySingleRow($sql, $array)) {
        return false;
    } else {
        return true;
    }
}

function executaQuery($sql, $array = null) {
    if (verificaFlagDebug()) {
        echo "Executando query: $sql: $array<br>";
    }
    $GLOBALS["contaQueries"] = $GLOBALS["contaQueries"] + 1;

    if (!isset($GLOBALS["DBH"])) {
        conectaPDO();
    }
    $stmt = $GLOBALS["DBH"]->prepare($sql);
    $stmt->execute($array);
    $stmt->setFetchMode(PDO::FETCH_ASSOC);

    return $stmt;
}

//retorna array
function executaQuerySingleRow($sql, $array = null) {
    $rs = executaQuery($sql, $array);
    $row = $rs->fetch();
    return $row;
}

function executaSQL($sql, $array = null) {
    if (verificaFlagDebug()) {
        echo "Executando SQL: $sql: $array<br>";
    }
    $GLOBALS["contaQueries"] = $GLOBALS["contaQueries"] + 1;

    try {
        if (!isset($GLOBALS["DBH"])) {
            conectaPDO();
        }
        $STH = $GLOBALS["DBH"]->prepare($sql);
        $STH->execute($array);
    } catch (PDOException $e) {
        logaError($e);
    }
    if (constainsSubstring($sql, "insert into")) {
        return getLastID();
    }
}

function getLastID() {
    return $GLOBALS["DBH"]->lastInsertId();
}

function logaError($e) {
    echo "Error:" . $e->getMessage();
    file_put_contents('PDOErrors.txt', $e->getMessage(), FILE_APPEND);
}

?>