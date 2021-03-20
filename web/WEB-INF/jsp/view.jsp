<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page import="com.urise.webapp.util.HtmlUtil" %>
<%@ page import="com.urise.webapp.model.SectionType" %>
<%@ page import="com.urise.webapp.model.sections.Section" %>
<%@ page import="com.urise.webapp.model.sections.ListSection" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="com.urise.webapp.model.sections.OrganizationSection" %>
<%@ page import="com.urise.webapp.model.Organization" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html" ; charset="UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>

    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit">Edit</a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<com.urise.webapp.model.ContactType, java.lang.String>"/>
                <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
    <p>

    <p>
        <c:forEach var="sectionEntry" items="${resume.sections}">
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<com.urise.webapp.model.SectionType, com.urise.webapp.model.sections.Section>"/>
                <% SectionType sectionType = sectionEntry.getKey();
                Section section = sectionEntry.getValue();
        String sectionString = null;
        switch (sectionType) {
            case PERSONAL:
            case OBJECTIVE:
                sectionString = section.toString();
                break;
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                sectionString = String.join(" </br> ", ((ListSection) section).getItems());
                break;
            case EDUCATION:
            case EXPERIENCE: {
                String headerOpen = "<h3>";
                String headerClose = "</h3>";

                StringBuffer buffer = new StringBuffer();
                ((OrganizationSection) section).getOrganizations().forEach(organization -> {
                    buffer.append(headerOpen).
                            append(HtmlUtil.linkOrName(organization)).
                            append(headerClose);

                    organization.getPositions().forEach(position -> {
                        buffer.append(headerOpen).append(position.getTitle()).
                                append(" : с ").
                                append(position.getStartDate()).
                                append(" по ").
                                append(position.getEndDate()).
                                append(headerClose).
                                append(HtmlUtil.notNull(position.getDescription())).
                                append("<br/>");
                    });
                });
                sectionString = buffer.toString();}}
    %>

    <h2><%=sectionType.getTitle()%>
    </h2>
    <%=sectionString%>
    <br/>
    </c:forEach>
    <p>

</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>