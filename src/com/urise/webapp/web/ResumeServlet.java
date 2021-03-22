package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.*;
import com.urise.webapp.model.sections.ListSection;
import com.urise.webapp.model.sections.OrganizationSection;
import com.urise.webapp.model.sections.Section;
import com.urise.webapp.model.sections.TextSection;
import com.urise.webapp.storage.Storage;
import com.urise.webapp.util.DateUtil;
import com.urise.webapp.util.HtmlUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ResumeServlet extends HttpServlet {

    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getDbStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");

        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }

        Resume resume;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
                resume = storage.get(uuid);
                break;
            case "add": {
                resume = Resume.getNewResume();
                break;
            }
            case "edit": {
                resume = storage.get(uuid);
                for (SectionType type : SectionType.values()) {
                    Section currentSection = resume.getSection(type);

                        switch (type) {
                            case PERSONAL:
                            case OBJECTIVE:
                                if (currentSection == null) {
                                    currentSection = new TextSection("");
                                }
                                break;
                            case ACHIEVEMENT:
                            case QUALIFICATIONS:
                                if (currentSection == null) {
                                    currentSection = new ListSection(new ArrayList<>());
                                }
                                break;
                            case EDUCATION:
                            case EXPERIENCE:
                                OrganizationSection organizations = (OrganizationSection) currentSection;
                                List<Organization> emptyOrganizations = new ArrayList<>();
                                emptyOrganizations.add(new Organization("", "", new Organization.Position()));
                                if (organizations != null) {
                                    for (Organization org : organizations.getOrganizations()) {
                                        List<Organization.Position> positions = new ArrayList<>();
                                        positions.add(new Organization.Position());
                                        positions.addAll(org.getPositions());
                                        emptyOrganizations.add(new Organization(org.getHomePage(), positions));
                                    }
                                }
                                currentSection = new OrganizationSection(emptyOrganizations);
                                break;
                        }
                        resume.addSection(type, currentSection);
                    }

                break;
            }
            default: {
                throw new IllegalArgumentException("Action " + action + " is illegal");
            }
        }
        request.setAttribute("resume", resume);
        request.getRequestDispatcher("view".equals(action) ?
                "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");

        // can not create or edit resume without full name
        if (fullName.trim().isEmpty()){
            response.sendRedirect("resume");
            return;
        }

        boolean isNew = (uuid.isEmpty());
        Resume resume;
        if (isNew) {
            resume = new Resume(fullName);
        } else {
            resume = storage.get(uuid);
            resume.setFullName(fullName);
        }

        // fill resume contacts from web-page
        for (ContactType contactType : ContactType.values()) {
            String value = request.getParameter(contactType.name());
            if (HtmlUtil.notExistItem(value)) {
                resume.getContacts().remove(contactType);
            } else {
                resume.addContact(contactType, value);
            }
        }

        //fill resume sections from web-page
        for (SectionType sectionType : SectionType.values()) {
            String value = request.getParameter(sectionType.name());
            String[] valueItems = request.getParameterValues(sectionType.name());
            if (HtmlUtil.notExistItem(value) && valueItems.length < 2) {
                resume.getSections().remove(sectionType);
                continue;
            }
            switch (sectionType) {
                case PERSONAL:
                case OBJECTIVE: {
                    resume.addSection(sectionType, new TextSection(value));
                    break;
                }
                case ACHIEVEMENT:
                case QUALIFICATIONS: {
                    List<String> sectionItems = readItems(value);
                    if (sectionItems != null) {
                        resume.addSection(sectionType, new ListSection(readItems(value)));
                    }
                    break;
                }
                case EXPERIENCE:
                case EDUCATION: {
                    List<Organization> organizations = new ArrayList<>();
                    String[] organizationUrl = request.getParameterValues(sectionType.name() + "link");

                    for (int i = 0; i < valueItems.length; i++) {
                        String organizationTitle = valueItems[i];
                        if (HtmlUtil.notExistItem(organizationTitle)) {
                            break;
                        }
                        Link organizationLink = new Link(organizationTitle, organizationUrl[i]);
                        List<Organization.Position> orgPositions = new ArrayList<>();

                        String positionOwner = sectionType.name() + i;
                        String[] positions = request.getParameterValues(positionOwner + "position");
                        String[] descriptions = request.getParameterValues(positionOwner + "desc");
                        String[] startDates = request.getParameterValues(positionOwner + "startDate");
                        String[] endDates = request.getParameterValues(positionOwner + "endDate");

                        if (positions.length > 0) {
                            for (int p = 0; p < positions.length; p++) {
                                orgPositions.add(new Organization.Position(DateUtil.parse(startDates[p]),
                                        DateUtil.parse(endDates[p]),
                                        positions[p], descriptions[p]));
                            }
                        }
                        organizations.add(new Organization(organizationLink, orgPositions));
                    }
                    resume.getSections().put(sectionType, new OrganizationSection(organizations));
                    break;
                }
            }
        }
           if (isNew){
               storage.save(resume);
           } else {
               storage.update(resume);
           }
        response.sendRedirect("resume");
    }

    private List<String> readItems(String value) {
        if (value.isEmpty() || value.trim().length() < 0 || value == null) return null;
        return Arrays.stream(value.split("\n")).filter(row -> row.trim().length() > 0).collect(Collectors.toList());
    }
}
