package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ResumeServlet extends HttpServlet {

    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getDbStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        PrintWriter writer = response.getWriter();
        String uuid = request.getParameter("name");
        if (uuid == null) {
            printResumeTable(writer);
        } else {
            printResume(writer, uuid);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private void printResume(PrintWriter writer, String uuid) {
        Resume currentResume = storage.get(uuid);
        writer.println(currentResume.toString());
    }

    private void printResumeTable(PrintWriter writer) {
        List<Resume> resumes = storage.getAllSorted();
        writer.println("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<style>\n" +
                "table {\n" +
                "  font-family: arial, sans-serif;\n" +
                "  border-collapse: collapse;\n" +
                "  width: 100%;\n" +
                "}\n" +
                "\n" +
                "td, th {\n" +
                "  border: 1px solid #dddddd;\n" +
                "  text-align: left;\n" +
                "  padding: 8px;\n" +
                "}\n" +
                "\n" +
                "tr:nth-child(even) {\n" +
                "  background-color: #dddddd;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h2>All resumes in database</h2>\n" +
                "\n" +
                "<table>\n" +
                "  <tr>\n");
        writer.println("<tr>\n<th>" + "Resume UUID </th>\n<th> Resumes owner fullname </th>\n</tr>\n");
        resumes.forEach(resume -> writer.println("<tr>\n<th>" + resume.getUuid() + "</th>\n<th>" + resume.getFullName() +
                "</th>\n</tr>\n"));
        writer.println(
                "</table>\n" +
                        "\n" +
                        "</body>\n" +
                        "</html>");
    }
}
