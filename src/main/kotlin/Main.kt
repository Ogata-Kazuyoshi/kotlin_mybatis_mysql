import database.*
import org.apache.ibatis.io.Resources
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.mybatis.dynamic.sql.SqlBuilder

fun main(args: Array<String>) {
    list5_4_22()
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

fun list5_4_10 () {
    val user = UserRecord(103,"Shiro",18,"Hello")
    createSessionFactory().openSession().use { session ->
        val mapper = session.getMapper(UserMapper::class.java)
        val count = mapper.insert(user)
        session.commit()
        println("${count}行のレコードを挿入しました")
    }
}

fun list5_4_12 () {
    val userList = listOf(UserRecord(104,"Goro",15,"Hello"), UserRecord(105,"Rokuro",13,"Hello"))
    createSessionFactory().openSession().use { session ->
        val mapper = session.getMapper(UserMapper::class.java)
        val count = mapper.insertMultiple(userList)
        session.commit()
        println("${count}行のレコードを挿入しました")
    }
}

fun list5_4_14 () {
    val user = UserRecord(id = 105, profile = "Bye")
    createSessionFactory().openSession().use { session ->
        val mapper = session.getMapper(UserMapper::class.java)
        val count = mapper.updateByPrimaryKeySelective(user)
        session.commit()
        println("${count}行のレコードを更新しました。")
    }
}

fun list5_4_16 () {
    createSessionFactory().openSession().use { session ->
        val mapper = session.getMapper(UserMapper::class.java)
        val count = mapper.update {
            set(UserDynamicSqlSupport.User.profile).equalTo("Hey")
            where(UserDynamicSqlSupport.User.id, SqlBuilder.isEqualTo(104))
        }
        session.commit()
        println("${count}行のレコードを更新しました")
    }
}

fun list5_4_18 () {
    val user = UserRecord(profile = "Good Morning")
    createSessionFactory().openSession().use { session ->
        val mapper = session.getMapper(UserMapper::class.java)
        val count = mapper.update {
            updateSelectiveColumns(user)
            where(UserDynamicSqlSupport.User.name, SqlBuilder.isEqualTo("Shiro"))
        }
        session.commit()
        println("${count}行のレコードを更新しました")
    }
}

fun list5_4_20 () {
    createSessionFactory().openSession().use { session ->
        val mapper = session.getMapper(UserMapper::class.java)
        val count = mapper.deleteByPrimaryKey(102)
        session.commit()
        println("${count} 行のレコードを削除しました")
    }
}

fun list5_4_22 () {
    createSessionFactory().openSession().use { session ->
        val mapper = session.getMapper(UserMapper::class.java)
        val count = mapper.delete {
            where(UserDynamicSqlSupport.User.name, SqlBuilder.isEqualTo("Jiro"))
        }
        session.commit()
        println("${count}行のレコードを削除しました")
    }
}