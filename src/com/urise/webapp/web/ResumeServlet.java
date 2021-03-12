package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ResumeServlet extends HttpServlet {

    private Storage storage = Config.get().getDbStorage();

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
        writer.println("<table style=\"width:100%\">");
        writer.println("<tr>\n<th>UUID</th>\n<th>Full name</th>\n </tr>\n");
        resumes.forEach(resume -> writer.println("<tr>\n<th>" + resume.getUuid() + "</th>\n<th>" + resume.getFullName() +
                "</th>\n</tr>\n"));
        writer.println("</table>");
    }
}
