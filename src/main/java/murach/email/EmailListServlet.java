package murach.email;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.util.GregorianCalendar;
import java.util.Calendar;

import murach.business.User;
import murach.data.UserDB;

@WebServlet("/emailList")
public class EmailListServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, 
                          HttpServletResponse response) 
                          throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String url = "/index.jsp";

        // Lấy action hiện tại
        String action = request.getParameter("action");
        if (action == null) {
            action = "join";  // action mặc định
        }

        // In thông báo debug ra Tomcat console
        System.out.println("EmailListServlet -- action=" + action);

        // Ghi thông báo debug vào Tomcat log file
        log("action=" + action);

        // Xử lý theo từng action
        if (action.equals("join")) {
            url = "/index.jsp";    // trang form đăng ký
        }
        else if (action.equals("add")) {                
            // Lấy tham số từ request
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");

            // Tạo đối tượng User từ dữ liệu nhập
            User user = new User(firstName, lastName, email);

            // Validate dữ liệu người dùng nhập
            String message;
            if (firstName == null || lastName == null || email == null ||
                firstName.trim().isEmpty() || lastName.trim().isEmpty() || email.trim().isEmpty()) {
                
                message = "Please fill out all three text boxes.";
                url = "/index.jsp";
            } 
            else {
                message = "";
                url = "/thanks.jsp";
                UserDB.insert(user);
            }
            
            request.setAttribute("user", user);
            request.setAttribute("message", message);
        }
        
        GregorianCalendar currentDate = new GregorianCalendar();
        int currentYear = currentDate.get(Calendar.YEAR);
        request.setAttribute("currentYear", currentYear);

        // Forward request và response tới trang chỉ định
        getServletContext()
            .getRequestDispatcher(url)
            .forward(request, response);
    }

    // Thêm doGet chuyển tiếp sang doPost
    @Override
    protected void doGet(HttpServletRequest request, 
                         HttpServletResponse response) 
                         throws ServletException, IOException {
        doPost(request, response);
    }    
}
