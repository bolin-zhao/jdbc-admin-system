package cn.bolin.bank.dao;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


/**
 * Create By Bolin on 12.11
 */
public class Customer extends BaseDAO{
    // 客户登录
    public boolean login (String username, String password){
        String sql = "select * from custinfo where cname = ? and cpassword = ?";
        Object[] args = {username,password};
        ResultSet rs = doexecuteQuery(sql, args);
        try {
            if (rs.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            this.closeConnection();
        }
        return false;
    }

    // 添加客户资料
    public int addCust(Object[] args){
        try{
            String sql = "insert into custinfo values(null,?,?,now(),now(),?,?,?)";
            return doexecuteUpdate(sql, args);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    // 查看客户资料
    public List viewCust(String name){
        Object [] args = {name};
        // 先根据用户名,查询出 所有用户资料
        String sql = "select * from custinfo where cname = ?";
        ResultSet rs = doexecuteQuery(sql, args);
        List list = new ArrayList();
        try{
            while (rs.next()){
                list.add(0, rs.getInt(1));
                list.add(1, rs.getString(2));
                list.add(2, rs.getString(3));
                list.add(3, rs.getTimestamp(4));
                list.add(4, rs.getTimestamp(5));
                list.add(5, rs.getString(6));
                list.add(6, rs.getString(7));
                list.add(7, rs.getString(8));
            }
        }catch(Exception e){
            e.printStackTrace();

        }
        return list;
    }

    // 更新个人资料(地址,邮箱,电话)
    public int updateCustm(Object[] args4){
        try{
            String sql = "update custinfo set caddress = ?,cemail = ?,cphone = ?  where cname = ?";
            return doexecuteUpdate(sql, args4);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    // 修改密码(根据客户名修改,先输入旧密码,正确后才能修改密码)
    public List editPwd(String name,String pwd1,String pwd2){
        // 先查询旧密码pwd1是否匹配,再修改新密码
        List list = new ArrayList();
        try{
        String sql1 = "select cpassword = ? from custinfo where cname = ?";
        String sql2 = "update custinfo set cpassword = ? where cname = ?";
        Object[] args1 = {pwd1,name};
        Object[] args2 = {pwd2,name};
            ResultSet rs1 = doexecuteQuery(sql1, args1);
            if (rs1.next()){
                list.add(0, rs1.getString(1));
            }
            if (list.get(0).equals("1")){
                doexecuteUpdate(sql2, args2);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    //  查询所有客户(根据客户名,余额模糊查询 1.vvip>10,000,000  2.vip >1000,000 3.normal <1000,000)
    public List searchCustm(String name){
        String sql1 = "select cid from custinfo where cname = ?";
        String sql2 = "select distinct a.abalance,a.level  from account a  inner join custinfo b on  a.acid  = ?";
        Object[] args = {name};
        ResultSet rs1 = doexecuteQuery(sql1, args);
        int cid;
        List list = new ArrayList();
        try {
            if (rs1.next()){
                cid = rs1.getInt(1);
                Object[] args2 = {cid};
                ResultSet rs2 = doexecuteQuery(sql2, args2);
                while (rs2.next()){
                    list.add(0, rs2.getDouble(1));
                    list.add(1, rs2.getInt(2));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 1.添加账户
        public int addAccount(String name){
            String SQL = "select cid from custinfo where cname = ?";
            Object[] args = {name};
            ResultSet rs = doexecuteQuery(SQL, args);
            int cid = 0;
            try {
                while (rs.next()) {
                    cid = rs.getInt("cid");
                }
                Object[] args1 = {cid};
                try{
                    String sql = "insert into account values(CEILING(RAND()*500000+500000),?,now(),0,1,3)";
                    return doexecuteUpdate(sql, args1);
                }catch (Exception e){
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                this.closeConnection();
            }
            return 0;
        }

     // 2. 查看账户资料
    public List searchAccount(int aid){
        String SQL = "select abalance,state from account where aid = ?";
        Object[] args = {aid};
        ResultSet rs = doexecuteQuery(SQL, args);
        try {
            while (rs.next()){
                Object object1 = rs.getObject(1);
                Object object2 = rs.getObject(2);
                List list = new ArrayList();
                list.add(0, object1);
                list.add(1, object2);
                return list;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 3. 存款
    public int deposit(double money, int aid) {
        String sql1 = "update account set abalance = abalance + ? where aid = ?";
        Object[] args1 = {money,aid};
        int i1 = doexecuteUpdate(sql1, args1);
        String SQL = "select abalance from account where aid = ?";
        Object[] args3 = {aid};
        ResultSet rs = doexecuteQuery(SQL, args3);
        try{
            if (rs.next()){
                money = rs.getDouble(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        String sql2 = "insert into log values(null, now(), ?, ?, 1, 1, ?)";
        Object[] args2 = {aid,aid,money};

        int i2 = doexecuteUpdate(sql2, args2);
        if (i1>0 && i2>0){
            return 1;
        }
        return 0;
    }

    // 4. 取款
    public int withdraw(double money, int aid) {
        String sql1 = "update account set abalance = abalance - ? where aid = ?";
        Object[] args1 = {money,aid};
        int i1 = doexecuteUpdate(sql1, args1);
        String SQL = "select abalance from account where aid = ?";
        Object[] args3 = {aid};
        ResultSet rs = doexecuteQuery(SQL, args3);
        try{
            if (rs.next()){
                money = rs.getDouble(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        String sql2 = "insert into log values(null, now(), ?, ?, 1, 1, ?)";
        Object[] args2 = {aid,aid,money};

        int i2 = doexecuteUpdate(sql2, args2);
        if (i1>0 && i2>0){
            return 1;
        }
        return 0;
    }

    // 5. 转账事务
    public int transMoney(Integer aidFrom, Integer aidTo, Double money){
        this.openConnection();
        String sql1 = "update account set abalance = abalance - ? where aid = ?;";
        String sql2 = "update account set abalance = abalance + ? where aid = ?;";
        String sql3 = "select abalance  from account where aid = ?;";
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        ResultSet rs = null;

        try {
            // 1. 开启事务
            conn.setAutoCommit(false);
            // 2. 转账涉及到两个账户金额变动
            ps1 = conn.prepareStatement(sql1);
            ps1.setDouble(1, money);
            ps1.setInt(2, aidFrom);
            int i1 = ps1.executeUpdate();

            ps2 = conn.prepareStatement(sql2);
            ps2.setDouble(1, money);
            ps2.setInt(2, aidTo);
            int i2 = ps2.executeUpdate();


            //检查转出方账户的余额是否足够支持此次转账金额；
            // 如果余额不足，则抛出“余额不足”异常，并回滚
            ps3 = conn.prepareStatement(sql3);
            ps3.setInt(1, aidFrom);
            rs = ps3.executeQuery();

            double abalance = 0;
            if (rs.next()){
                abalance = rs.getDouble("abalance");
            }
            if (abalance<0){
                try {
                    throw new Exception("账户余额不足!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            conn.commit();

            // 提交后打印日志
            String sql4 = "insert into log values(null, now(), ?, ?, 1, 1, ?)";
            Object[] args2 = {aidFrom,aidTo,abalance};
            int i3 = doexecuteUpdate(sql4, args2);
            if (i1>0 && i2>0 && i3>0){
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 6. 注销账户
    public List logOut(Integer num){
        this.openConnection();
        String sql = "update account set state = 2  where aid = ?";
        String SQL = "select abalance from account where aid = ?";
        Object[] args = {num};
        int rs = doexecuteUpdate(sql, args);
        ResultSet RS = doexecuteQuery(SQL, args);
        List list = new ArrayList();
        try{
            if(RS.next()){
                double a1 = RS.getDouble(1);
                list.add(0, rs);
                list.add(1, a1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        for (Object o : list) {
            System.out.println(o);
        }
        return list;
    }

    // 7. 激活账号
    public List activeAccount(Integer num){
        this.openConnection();
        String sql = "update account set state = 1, abalance = abalance -10  where aid = ?";
        String SQL = "select abalance from account where aid = ?";
        Object[] args = {num};
        int rs = doexecuteUpdate(sql, args);
        ResultSet RS = doexecuteQuery(SQL, args);
        List list = new ArrayList();
        try{
            if(RS.next()){
                double a1 = RS.getDouble(1);
                list.add(0, rs);
                list.add(1, a1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        /*for (Object o : list) {
            System.out.println(o);
        }*/
        return list;
    }

    // 3.2 查看日志列表
    public List viewLog(int num){
         Object [] args = {num};
        // 先根据账号aid,查询出 raid 再根据raid,查询出日志列表
        String sql = "SELECT distinct c.* FROM \n" +
                "log c \n" +
                "INNER JOIN \n" +
                "account a \n" +
                "ON c.raidfrom = ? \n" +
                "order by rid";
        ResultSet rs = doexecuteQuery(sql, args);
        List list = new ArrayList();
        try{
            while (rs.next()){
                list.add(0, rs.getInt(1));
                list.add(1, rs.getTimestamp(2));
                list.add(2, rs.getInt(3));
                list.add(3, rs.getInt(4));
                list.add(4, rs.getInt(5));
                list.add(5, rs.getInt(6));
                list.add(6, rs.getDouble(7));
            }
        }catch(Exception e){
            e.printStackTrace();

        }
        return list;
    }
}
