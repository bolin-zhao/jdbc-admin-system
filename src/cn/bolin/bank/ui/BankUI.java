package cn.bolin.bank.ui;
import cn.bolin.bank.dao.Customer;
import java.util.List;
import java.util.Scanner;

/**
 * Create By Bolin on 12.10
 */
public class BankUI {
    static Customer cust = new Customer();
    static Scanner input = new Scanner(System.in);

    // 登录主界面
    public static void main(String[] args) {
        System.out.println("*****************欢迎登陆天地银行系统!*********************");
        System.out.println();
        System.out.println("             1. 客户管理       2. 卡号管理");
        System.out.println();
        System.out.println("             3. 日志管理       4. 退    出");
        System.out.println();
        System.out.println("*********************************************************");
        System.out.print("请输入数字: ");
        int num = input.nextInt();
        switch (num){
            case 1:
                // 客户管理
                printCustManageMenu();
                break;
            case 2:
                System.out.println("提示: 管理卡号需要客户登录!  请输入: ");
                // 卡号管理
                custLogin();
                break;
            case 3:
                // 日志管理:
                printLogMenu();
                break;
            case 4:
                // 退出系统
                System.out.println("退出系统, bye!");
                System.exit(1);
                break;
            default:
                System.out.println("输入有误!");
                break;
        }
        // 1. 客户管理,添加客户成功后
        System.out.println("******************************************");
        System.out.println("1.返回客户管理界面  2.返回主菜单 3.返回日志管理 4.退出系统");
        System.out.println("请输入数字: ");
        int answer = input.nextInt();
        if (answer == 1){
            // 返回客户管理界面
            printCustManageMenu();
        }else if (answer == 2){
            // 返回主菜单
            main(args);
        }else if(answer == 3){
            // 返回日志管理界面
            printLogMenu();
        }else if(answer == 4){
            // 退出系统
            System.exit(1);
        }else {
            System.out.println("输入有误!");
            // 退出系统
            System.exit(1);
        }
    }

    // 一. 打印客户管理菜单
    public static void printCustManageMenu(){
        System.out.println("*****************欢迎来到客户管理界面************************");
        System.out.println();
        System.out.println("          1. 添 加 客 户        2. 客 户 登 录");
        System.out.println();
        System.out.println("          3. 更新客户资料       4. 查看客户个人资料  ");
        System.out.println();
        System.out.println("          5. 修改客户密码       6. 查询所有客户  ");
        System.out.println();
        System.out.println("*********************************************************");
        System.out.print("请输入数字: ");
        int num = input.nextInt();
        switch(num){
            case 1:
                // 添加客户
                addCust();
                break;
            case 2:
                // 客户登录
                custLogin();
                break;
            case 3:
                // 更新客户资料
                updateCust();
                break;
            case 4:
                // 查看客户户个人资料
                viewCustm();
                break;
            case 5:
                // 修改客户密码
                editPwd();
                break;
            case 6:
                // 查看所有客户
                searchCus();
                break;
            default:
                System.out.println("输入有误!");
                break;
        }

    }

    // 2. 客户登录
    static int custLogCount = 0;
    public static void custLogin(){
        System.out.println("账户名: ");
        String name = input.next();
        System.out.println("密码: ");
        String password = input.next();
        boolean isFlag = cust.login(name,password);
        if (isFlag) {
            custLogCount = 0;
            System.out.println("恭喜\t"+ name +"\t登陆成功!");
            printAccountManageMenu();
        }else {
            System.out.println("抱歉! 亲爱的"+ name +" 您的账户密码不匹配!");
            custLogCount++;
            if (custLogCount<3){
                System.out.println("请重新输入, 还剩余"+ (3-custLogCount) +"次机会: ");
                custLogin();
            }else {
                System.out.println("输入错误三次,系统终止...");
                System.exit(1);
            }
        }
    }

    // 1. 添加客户资料
    public static void addCust(){
        System.out.println("客户名称: ");
        String cname = input.next();
        System.out.println("登录密码:");
        String cpassword = input.next();
        System.out.println("地 址: ");
        String caddress = input.next();
        System.out.println("邮 箱: ");
        String cemail = input.next();
        System.out.println("电 话: ");
        String cphone = input.next();
        // 添加上次登录时间
        // 注册日期
        System.out.println("温馨提示: 添加新用户时,上次登陆时间和注册时间自动设为本地系统时间");
        Object[] args = {cname,cpassword,caddress,cemail,cphone};
        int count = cust.addCust(args);
        if(count>0) {
            System.out.println("添加客户成功!");
        }else {
            System.out.println("添加失败!");
        }
    }

    // 3. 更新客户资料
    public static void updateCust(){
        System.out.println("请输入要更新资料的客户名:");
        String cname = input.next();
        System.out.println("地址: ");
        String caddress = input.next();
        System.out.println("邮箱: ");
        String cemail = input.next();
        System.out.println("电话: ");
        String cphone = input.next();
        Object[] args4 = {caddress,cemail,cphone,cname};
        int count = cust.updateCustm(args4);
        if(count>0) {
            System.out.println("更新客户成功!");
        }else {
            System.out.println("更新失败!");
        }
    }

    // 4. 查看客户个人资料
    public static void viewCustm(){
        System.out.println("请输入要查询的用户名: ");
        String name = input.next();
        List list = cust.viewCust(name);
        System.out.println("查询成功!");
        // System.out.println(list.size());
        System.out.println("******************************************");
        System.out.println("编号  |客户名|  密码    |    注册日期    |   上次登录日期  |地址|邮箱|电话");
        for (int i = 0; i <list.size() ; i++) {
            System.out.println( list.get(i)+", "+list.get(++i)+",  "+list.get(++i)+"," +list.get(++i) +","+list.get(++i)+","+list.get(++i)+","+list.get(++i)+","+list.get(++i));
        }
    }

    // 5. 修改客户密码
    public static void editPwd(){
        System.out.println("请输入要修改密码的用户名: ");
        String name = input.next();
        System.out.println("请输入旧密码: ");
        String pwd1 = input.next();
        System.out.println("请输入新密码: ");
        String pwd2 = input.next();
        List list = cust.editPwd(name, pwd1, pwd2);
        System.out.println(list.get(0));
        if(list.get(0).equals("1")) {
            System.out.println("修改密码成功!");
        }else {
            System.out.println("旧密码输入有误,修改密码失败!");
        }
    }

    // 6. 查询客户
    public static void searchCus(){
        System.out.println("请输入查询的客户名: ");
        String name = input.next();
        List list = cust.searchCustm(name);
        System.out.println(list);
        System.out.println("余额        |    用户等级");
        double sum = 0;
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i)+" RMB     |     "+list.get(++i));
            if (i*2>list.size()){
                System.out.println("*****************************************");
                System.out.println("总余额为: "+sum);
                if (sum>=10000000){
                    System.out.println(name + ", 您是尊贵的 vvip 客户, 欢迎您亲自来视察!");
                    System.out.println("余额:" + sum +"RMB");
                }else if (sum<=1000000){
                    System.out.println("余额::"+ sum +"RMB");
                    System.out.println(name + ", 您只是普通客户!");
                }else {
                    System.out.println(name + ", 您是尊敬的 vip 客户, 欢迎光临! ");
                    System.out.println("余额:" + sum +"RMB");
                }
                return ;
            }
            sum += (double)list.get(i*2);
        }

    }

    // 二. 打印卡号(账号)管理菜单
    public static void  printAccountManageMenu(){
        System.out.println("*********************欢迎来到账户管理界面******************");
        System.out.println();
        System.out.println("            1. 添加账号                    2. 查看账号资料");
        System.out.println("                 3. 存  款             4. 取  款");
        System.out.println("                    5. 转  账      6. 注销账号");
        System.out.println("                          7. 激活账号  ");
        System.out.println();
        System.out.println("*********************************************************");
        System.out.println("请输入数字: ");
        int num = input.nextInt();
        switch(num){
            case 1:
                // 添加账号
                addAccount();
                break;
            case 2:
                // 查看资料
                searchAccount();
                break;
            case 3:
                // 存款
                deposit();
                break;
            case 4:
                // 取款
                withdraw();
                break;
            case 5:
                // 转账
                transMoney();
                break;
            case 6:
                // 注销账户
                logOut();
                break;
            case 7:
                activeAccount();
                // 激活账号
                break;
            default:
                System.out.println("输入有误!");
                break;
        }
    }

    // 1. 添加账户信息
    public static void addAccount(){
        System.out.println("温馨提示: 添加账户时,添加日期自动设为本地系统时间");
        // 输入客户名,获得cid,选择数字1,自动创建账户
        System.out.println("请输入要添加账户的客户名: ");
        String name = input.next();
        int count = cust.addAccount(name);
        if(count>0) {
            System.out.println("添加客户成功!");
        }else {
            System.out.println("添加失败!");
        }

    }

    // 2. 查看账户资料
    // 根据账号查看当前账号的信息。如:余额、账号状态等.
    public static void searchAccount(){
        System.out.println("请输入要查询的账号aid: ");
        int aid = input.nextInt();
        List list;
        list = cust.searchAccount(aid);
        System.out.println("您的账户余额为:" + list.get(0) + "RMB");
        int state = (int) list.get(1);
        if (state == 1){
            System.out.println("账户状态为: 正常");
        }else if (state == 0){
            System.out.println("此账户已经被注销");
        }

    }

    // 3.存款
    // 客户可以为自己账号存钱操作(当用户的账号余额满足vvip符合条件时，自动提升或降低客户等级)
    public static void deposit(){
        System.out.println("请输入存款的卡号: ");
        int aid = input.nextInt();
        System.out.println("请输入存款金额: ");
        double money = input.nextDouble();
        int deposit = cust.deposit(money,aid);
        if (deposit >0){
            System.out.println("存款成功!");
        }else {
            System.out.println("存款失败!");
        }
        System.out.println("提示: 交易记录已添加,请前往日志页查看!");
    }

    // 4.取款
    public static void withdraw(){
        System.out.println("请输入取款的卡号: ");
        int aid = input.nextInt();
        System.out.println("请输入取款金额: ");
        double money = input.nextDouble();
        int withdraw = cust.withdraw(money,aid);
        if (withdraw >0){
            System.out.println("取款成功!");
        }else {
            System.out.println("取款失败!");
        }
        System.out.println("提示: 交易记录已添加,请前往日志页查看!");
    }

    // 5.转账
    public static void transMoney(){

        System.out.println("请输入取款的卡号: ");
        int aidFrom = input.nextInt();
        System.out.println("请输入转入的卡号: ");
        int aidTo = input.nextInt();
        System.out.println("请输入转账的金额: ");
        double money = input.nextDouble();
        int result = cust.transMoney(aidFrom, aidTo, money);
        if (result>0){
            System.out.println("转账成功!");
        }else {
            System.out.println("转账失败!");
        }
        System.out.println("提示: 交易记录已添加,请前往日志页查看!");
    }

    // 6. 注销账户
    // 客户可以注销其下账号。账号注销之后，
    // 不是删除，而是修改其状态state。
    // 注销的时候要判断一下账号金额哦！难道都想把钱给银行？并且在存款、取款、转账时候要判断是否是已注销账号）
    public static void logOut(){
        System.out.println("请输入要注销的卡号: ");
        int num = input.nextInt();
        List list;
        list = cust.logOut(num);
        int result = (int) list.get(0);
        double balance = (double) list.get(1);
        if (result > 0){
            if (balance <= 0) {
                System.out.println("注销成功!");

            }else {
                System.out.println("您的卡里还有余额,"+ balance +"确定注销(确定/取消)?");
                String isLogOut = input.next();
                if (isLogOut.equals("确定")){
                    System.out.println("注销成功!");
                }else if (isLogOut.equals("取消")){
                    System.out.println("注销已取消!");
                    printAccountManageMenu();
                }
            }
        }else {
            System.out.println("注销失败!");
        }
    }

    // 7. 激活账号
    //该登录用户其下注销账号恢复正常使用，并缴纳10元手续费。
    public static void activeAccount(){
        System.out.println("请输入要激活的卡号: ");
        int num = input.nextInt();
        List list;
        list = cust.activeAccount(num);
        int result = (int) list.get(0);
        double balance = (double) list.get(1);
        if (result > 0){
            if (balance <= 10) {
                System.out.println("激活账户需要缴纳10元工本费,您的余额已不足...");
                System.out.println("激活失败!");
            }else {
                System.out.println("账户已成功激活!");
                // 账户余额减10元


            }
        }else {
            System.out.println("注销失败!");
        }
    }

    // 三. 打印日志管理菜单
    public static void printLogMenu(){
        System.out.println("*******************交易日志管理界面**********************");
        System.out.println();
        System.out.println("                  1. 添加日志记录");
        System.out.println();
        System.out.println("                  2. 查看账号交易记录");
        System.out.println();
        System.out.println("********************************************************");
        System.out.println("请输入数字: ");
        int num = input.nextInt();
        switch (num){
            case 1:
                System.out.println("日志无法单独添加,请选择下一步操作:");
                System.out.println("1.转账 2.取款 3.存款 4.退出");
                int num1 = input.nextInt();
                switch(num1){
                    case 1:
                        // 转账
                        transMoney();
                        break;
                    case 2:
                        // 取款
                        withdraw();
                        break;
                    case 3:
                        // 存款
                        deposit();
                        break;
                    case 4:
                        // 退出
                        System.out.println("退出系统!");
                        System.exit(1);
                }
                break;
            case 2:
                // 查看账户交易记录
                viewPayLog();
                break;
            default:
                System.out.println("输入有误!");
                break;
        }
    }

    // 3.0 生成交易日志
    public static void createPayLog(){
        System.out.println("自动生成的日志:");
    }

    // 3.1查看交易日志(根据账号aid)
    public static void viewPayLog(){
        System.out.println("提示信息: 查看日志需要登录");
        System.out.println("请输入要查看的客户名: ");
        String name = input.next();
        System.out.println("请输入密码: ");
        String pwd = input.next();
        boolean isFlag = cust.login(name,pwd);
        if (isFlag) {
            custLogCount = 0;
            System.out.println("恭喜\t"+ name +"\t登陆成功!");
            // 登录成功后,显示该用户的日志列表
            viewLog();
        }else {
            System.out.println("抱歉! 亲爱的"+ name +" 您的账户密码不匹配!");
            custLogCount++;
            // 登陆失败,重新登录,未设置次数限制
            viewPayLog();
        }
    }

    // 3.2 查看日志(根据用户的卡号获取日志记录)
    public static void viewLog(){
        System.out.println("请输入要查看的卡号: ");
        int num = input.nextInt();
        List list = cust.viewLog(num);
        System.out.println("查询成功!");
        System.out.println(list.size());
        System.out.println("******************************************");
        System.out.println("编号  |   日期     |      转账账号|收款账号|交易类型|交易状态|  余额 ");
        for (int i = 0; i <list.size() ; i++) {
            System.out.println( list.get(i)+", "+list.get(++i)+",  "+list.get(++i)+"," +list.get(++i) +",     "+list.get(++i)+",      "+list.get(++i)+",  "+list.get(++i));
        }
    }
}
