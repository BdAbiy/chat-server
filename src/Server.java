/**
 * simple Tcp server
 * handles connections
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;



import net.sf.json.JSONObject;


public class Server {

    public Server(){
        try{
            ServerSocket server = new ServerSocket(8080);
            while (true) {
                Socket client = server.accept();
                

            }
            
            


        }catch(Exception e){}
        

     }
     private static int HandleClient(Socket client){
        try {
        BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String line ="";
                StringBuilder request = new StringBuilder();
                while ((line = reader.readLine())!= null) {
                    request.append(line).append("\n");

                }
                JSONObject json = new JSONObject().fromObject(request.toString());
                String methode = "";
                String user = "";
                String pass = "";
                if (
                 (String)json.get("methode") != null &&
                 (String)json.get("username") != null &&
                 (String)json.get("password")!= null ){
                    methode= (String)json.get("methode") ;
                    user = (String)json.get("username");
                    pass = (String)json.get("password");
                 }else {client.getOutputStream().write("Bad request ! you should send a  (methode : login or sign up or delete ) and (user : the account username) and (password : the password) with json)".getBytes());client.close();}

                if (methode.equals("Sign up")){
                    
                        if(Users().containsKey(user)){
                            client.getOutputStream().write("user name already taken".getBytes());client.close();
                        }else {
                        ManageUsers(0,user,pass);}

                    }if (methode.equals("login")){
                    
                        if(Users().containsKey(user)){
                            if (Users().get(user).equals(pass)){
                                client.getOutputStream().write("successfully logged in".getBytes());
                                
                            }else{client.getOutputStream().write("Wrong password".getBytes());client.close();}
                        }else {
                        ManageUsers(0,user,pass);}

                    }


            }catch(Exception e){}
        return 0 ;
     }
     public static JSONObject Users(){
        StringBuilder res = new StringBuilder() ;
        try {
            File f = new File("src/users.json");
            BufferedReader r =new BufferedReader(new FileReader(f));
            String l ;
            while ((l = r.readLine()) != null) {
            res.append(l).append("\n");
            }r.close();
        }catch(Exception e){
        return null;
    }
        return new JSONObject().fromObject(res.toString());
}

    private static void ManageUsers(int methode,String user,String password){
        switch (methode) {
            case 0:
                Users().put(user, password);
            case 1:
                Users().remove(user);
        
        }

    }
}