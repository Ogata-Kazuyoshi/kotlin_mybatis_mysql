import database.*
import org.apache.ibatis.io.Resources
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.mybatis.dynamic.sql.SqlBuilder

fun main(args: Array<String>) {
    list5_4_9()
}

fun createSessionFactory():SqlSessionFactory {
    val resource = "mybatis-config.xml"
    var inputStream = Resources.getResourceAsStream(resource)
    return SqlSessionFactoryBuilder().build(inputStream)
}

fun list5_4_3 () {
    createSessionFactory().openSession().use { session ->
        //createSessionFactory().openSession()の部分でデータベースへの接続を確立
        //opensessionにより生成されたsessionを引数としてラムダ式の中で使用する
        val mapper = session.getMapper(UserMapper::class.java)
        //getMapper(UserMappar::class.java)でuserテーブルに対するmapperオブジェクトを取得
        val user = mapper.selectByPrimaryKey(100)
        //mapperのselectByPrimaryKey関数で、userテーブルの主キー検索を実行
        println(user)
    }
}

fun list5_4_4 () {
    createSessionFactory().openSession().use {session ->
        val mapper = session.getMapper(UserMapper::class.java)
        val userList = mapper.select {
            where(UserDynamicSqlSupport.User.name, SqlBuilder.isEqualTo("Jiro"))
            //where句でnameカラムがJiroのものだけに絞る.Listで返ってくることに注意
        }
        println(userList)
    }
}

fun list5_4_6 () {
    createSessionFactory().openSession().use {session ->
        val mapper = session.getMapper(UserMapper::class.java)
        val userList = mapper.select {
            where(UserDynamicSqlSupport.User.age, SqlBuilder.isGreaterThanOrEqualTo(25))
        }
        println(userList)
    }
}

fun list5_4_8 () {
    createSessionFactory().openSession().use { session ->
        val mapper = session.getMapper(UserMapper::class.java)
        val count = mapper.count {
            where(UserDynamicSqlSupport.User.age, SqlBuilder.isGreaterThanOrEqualTo(25))
        }
        println(count)
    }
}

fun list5_4_9 () {
    createSessionFactory().openSession().use { session ->
        val mapper = session.getMapper(UserMapper::class.java)
        val count = mapper.count {
            allRows()
            //全行を返却
        }
        println(count)
    }
}