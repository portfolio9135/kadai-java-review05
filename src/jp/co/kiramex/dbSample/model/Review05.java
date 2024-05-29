package jp.co.kiramex.dbSample.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Review05 {

    public static void main(String[] args) {
        // DB接続情報 変数まとめとく
        String url = "jdbc:mysql://localhost/kadaidb?useSSL=false&allowPublicKeyRetrieval=true";
        String user = "root";
        String password = "Ts91358922";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Scanner scanner = null;

        try {
            // 1. ドライバのクラスをJava上で読み込む これがないとデータベースに接続できない
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. ここでDBと接続! url user passは変数まとめで格納したもの。これを指定して固有のDBに接続する
            con = DriverManager.getConnection(url, user, password);

            // ここでidを入力させてその値を取得
            scanner = new Scanner(System.in);
            System.out.print("検索キーワードを入力してください > ");
            String input = scanner.nextLine();

            // 入力された文字列を数値に変換
            //ユーザーが入力した値を数値に変換してる。文字列が数値に変換できなかったらエラーになる
            //型は大事。
            int id = Integer.parseInt(input);

            // 3. SQL文を準備する
            //DBからデータを取得するためのSQL文を準備してる
            //?の部分に入力されたidをセットしてる
            String sql = "SELECT name, age FROM person WHERE id = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);

            // 4. SQL文を実行する
            //準備したSQLを実行して結果を「変数rs」に代入
            rs = pstmt.executeQuery();

            // 5. 結果を表示する
            //結果があれば、name と age を表示してる。なかったら「データが見つかりませんでした。」って表示する。
            if (rs.next()) {
                String name = rs.getString("name");
                int age = rs.getInt("age");
                System.out.println(name);
                System.out.println(age);
            } else {
                System.out.println("データが見つかりませんでした。");
            }

            //プログラム実行中に起こり得るエラーをキャッチしてそのエラーに合わせて適切なメッセージを表示する

        } catch (ClassNotFoundException e) {
            System.err.println("JDBCドライバのロードに失敗しました。"); //これは、JDBCドライバを読み込もうとしたときにクラスが見つからなかった場合に発生するエラーをキャッチするブロック
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("データベースに異常が発生しました。");//これは、データベース操作中に発生するSQL関連のエラーをキャッチするブロック
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("入力されたIDが不正です。数値を入力してください。");//これは、ユーザーが入力したIDを数値に変換しようとしたときに発生するエラーをキャッチするブロック
            e.printStackTrace();

            //finally ブロックは、tryブロック内で例外が発生したかどうかに関係なく、必ず実行される部分
        } finally {

            //最後にデータベースの接続やリソースを閉じる
            //これをしないとリソースリーク？って問題が起こる
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (con != null) {
                    con.close();
                }
                if (scanner != null) {
                    scanner.close();
                }
            } catch (SQLException e) {
                System.err.println("データベース切断時にエラーが発生しました。");
                e.printStackTrace();
            }
        }
    }
}
