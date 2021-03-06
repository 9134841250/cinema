package ru.nstu.cinema.server;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.nstu.cinema.common.entity.Film;
import ru.nstu.cinema.common.entity.Hall;
import ru.nstu.cinema.common.entity.Seat;
import ru.nstu.cinema.common.entity.Session;

import java.sql.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ksuhan on 04.06.17.
 */
public class CinemaStorage {
    // JDBC driver name and database URL
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/cinema";

    //  Database credentials
    private static final String USER = "username";
    private static final String PASS = "password";

    enum databases{hall,film,session,seats}
    enum hallFields{id, name, descr, struct}
    enum filmFields{id, name, descr}
    enum sessionFields{id, session_time, hall_id,film_id, price}
    enum seatsFields{id, session_id, row,seat}

    private JSONParser parser=new JSONParser();

    private JSONObject getJSONObjectFromString(String str) throws ParseException {
        return (JSONObject)parser.parse(str);
    }

    /***
     * Добавить зал в БД
     * @param hall объект описания зала кинотеатра
     * @return
     */
    public Hall addHall(Hall hall){
        Connection conn = null;
        PreparedStatement stmt = null;
        try{
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");

            String sql;
            sql = String.format("INSERT INTO %s ( %s, %s, %s ) VALUES ( ? , ? , ?)",
                    databases.hall,
                    hallFields.name,
                    hallFields.descr,
                    hallFields.struct);
            stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, hall.getName());
            stmt.setString(2, hall.getDescription());
            stmt.setString(3, hall.getStructure().toString());
            stmt.execute();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                hall.setId(generatedKeys.getInt(1));
            }
            else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }
            stmt.close();
            conn.close();
        } catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException ignored){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
        return hall;
    }

    /***
     * Добавить фильм
     * @param film объект описания фильма
     * @return
     */
    public Film addFilm(Film film) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");

            String sql;
            sql = String.format("INSERT INTO %s ( %s, %s ) VALUES ( ? , ? )",
                    databases.film,
                    filmFields.name,
                    filmFields.descr);
            stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescr().toString());
            stmt.execute();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    film.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            stmt.close();
            conn.close();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException ignored) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
        return film;
    }

    /***
     * Добавить сеанс в БД
     * @param session
     * @return
     */
      public Session addSession(Session session){
        Connection conn = null;
        PreparedStatement stmt = null;
        try{
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");

            String sql;
            sql = String.format("INSERT INTO %s ( %s, %s, %s, %s ) VALUES ( ? , ? , ? , ?  )",
                    databases.session,
                    sessionFields.session_time,
                    sessionFields.hall_id,
                    sessionFields.film_id,
                    sessionFields.price
                    );
            stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            Timestamp timestamp =Timestamp.valueOf(session.getTime());
            stmt.setTimestamp(1, timestamp);
            stmt.setInt(2, session.getHall().getId());
            stmt.setInt(3, session.getFilm().getId());
            stmt.setInt(4, session.getPrice());
            stmt.execute();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    session.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

            stmt.close();
            conn.close();
        } catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException ignored){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
        return session;
    }

    /***
     * Добавить инфу о занятом месте в БД
     * @param seat место в зале
     */
    public void addSeat(Seat seat) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");

            String sql;
            sql = String.format("INSERT INTO %s ( %s, %s, %s ) VALUES ( ? , ? , ?   )",
                    databases.seats,
                    seatsFields.session_id,
                    seatsFields.row,
                    seatsFields.seat
            );
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, seat.getSession().getId());
            stmt.setInt(2, seat.getRow());
            stmt.setInt(3, seat.getSeat());
            stmt.execute();


            stmt.close();
            conn.close();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException ignored) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");

    }

    /**
     * Вывести список всех залов(нужен ли)
     * @return
     */
    public List<Hall> retrieveHalls(){
        Connection conn = null;
        Statement stmt = null;
        ArrayList<Hall>result= new ArrayList<>();
        try{
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;

            sql = String.format("SELECT %s, %s, %s, %s FROM %s",
                    hallFields.id,
                    hallFields.name,
                    hallFields.descr,
                    hallFields.struct,
                    databases.hall);
            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from result set
            while(rs.next()){
                //Retrieve by column name
                int id  = rs.getInt( hallFields.id.name() );
                String name = rs.getString(  hallFields.name.name() );
                String descr = rs.getString( hallFields.descr.name() );
                String struct = rs.getString( hallFields.struct.name() );
                JSONObject jsonObject=getJSONObjectFromString(struct);

                result.add( new Hall(id,  name, descr, jsonObject ));

                //Display values
                //System.out.print("ID: " + id);
                //System.out.print(", name: " + name);
                //System.out.print(", descr: " + descr);
                //System.out.println(", struct: " + struct);
            }
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
        return result;
    }

    /***
     * Вывести список всех фильмов
     * @return
     */
    public List<Film> retrieveFilms(){
        Connection conn = null;
        Statement stmt = null;
        ArrayList<Film>result= new ArrayList<>();
        try{
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;

            sql = String.format("SELECT %s, %s, %s FROM %s",
                    hallFields.id,
                    hallFields.name,
                    hallFields.descr,
                    databases.film);
            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from result set
            while(rs.next()){
                //Retrieve by column name
                int id  = rs.getInt( filmFields.id.name() );
                String name = rs.getString(  filmFields.name.name() );
                String descr = rs.getString( filmFields.descr.name() );
                JSONObject jsonObject=getJSONObjectFromString(descr);

                result.add( new Film(id,  name, jsonObject ));

                //Display values
                //System.out.print("ID: " + id);
                //System.out.print(", name: " + name);
                //System.out.print(", descr: " + descr);
            }
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
        return result;

    }

    /**
     * Вывести список всех сеансов
     * @return список сеансов
     */
    public List<Session> retrieveSessions(){
        Connection conn = null;
        PreparedStatement stmt = null;
        ArrayList<Session>result= new ArrayList<>();
        try{
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");

            String sql;

            sql = String.format("SELECT a.%s, a.%s, a.%s, a.%s, a.%s," +
                            " b.%s,b.%s,b.%s, " +
                            " c.%s, c.%s, c.%s, c.%s"+
                            " FROM %s a" +
                            " LEFT JOIN %s b" +
                            " ON (a.%s=b.%s) " +
                            " LEFT JOIN %s c" +
                            " ON (a.%s=c.%s) " ,
                    sessionFields.id, sessionFields.session_time, sessionFields.hall_id,sessionFields.film_id, sessionFields.price,
                    filmFields.id,filmFields.name,filmFields.descr,
                    hallFields.id,hallFields.name,hallFields.descr,hallFields.struct,
                    databases.session,
                    databases.film,
                    sessionFields.film_id,filmFields.id,
                    databases.hall,
                    sessionFields.hall_id,hallFields.id);
            stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from result set
            while(rs.next()){
                //Retrieve by column name
                int id  = rs.getInt( 1 );
                java.sql.Timestamp timestamp=rs.getTimestamp(2);
                LocalDateTime session_time=timestamp.toLocalDateTime();
                int hall_id = rs.getInt( 3 );
                int film_id = rs.getInt( 4 );
                int price = rs.getInt( 5 );

                int filmId=rs.getInt(6);
                String filmName=rs.getString(7);
                String filmDescr=rs.getString(8);

                Film film=new Film(filmId,filmName,getJSONObjectFromString(filmDescr));

                int hallId=rs.getInt(9);
                String hallName=rs.getString(10);
                String hallDescr=rs.getString(11);
                String hallStruct=rs.getString(12);

                Hall hall=new Hall(hallId,hallName,hallDescr,getJSONObjectFromString(hallStruct));
                result.add( new Session(id,film,hall,session_time, price));

            }
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
        return result;
    }

    public List<Seat> retrieveSeats(Session session) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ArrayList<Seat> result = new ArrayList<>();
        try {
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");

            String sql;

            sql = String.format("SELECT %s, %s, %s, %s" +
                            " FROM %s " +
                            " WHERE %s=? ;",
                    seatsFields.id, seatsFields.session_id, seatsFields.row, seatsFields.seat,
                    databases.seats,
                    seatsFields.session_id);
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, session.getId());
            ResultSet rs = stmt.executeQuery();

            //STEP 5: Extract data from result set
            while (rs.next()) {
                //Retrieve by column name
                int id = rs.getInt(1);
                int session_id = rs.getInt(2);
                int row = rs.getInt(3);
                int seat = rs.getInt(4);


                result.add(new Seat(id, session, row, seat));


            }
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
        return result;
    }


}
