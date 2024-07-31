
import com.mukesh.login.helper.JwtTokenUtil;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ermuk
 */
public class JwtTokenUtilTest {
    public static void main(String[] args) {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        String token = jwtTokenUtil.generateToken("testuser");
        System.out.println("Generated Token: " + token);
    }
}
