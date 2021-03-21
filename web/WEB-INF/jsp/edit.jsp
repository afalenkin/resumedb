<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page import="com.urise.webapp.model.SectionType" %>
<%@ page import="com.urise.webapp.util.HtmlUtil" %>
<%@ page import="com.urise.webapp.model.sections.ListSection" %>
<%@ page import="com.urise.webapp.model.sections.OrganizationSection" %>
<%@ page import="com.urise.webapp.util.DateUtil" %>
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

    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">

        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size=50 value="${resume.fullName}"></dd>
        </dl>

        <h3>Контакты:</h3>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type.name()}" size=30 value="${resume.getContact(type)}"></dd>
            </dl>
        </c:forEach>

        <h3>Секции:</h3>
        <c:forEach var="sectionType" items="<%=SectionType.values()%>">
            <c:set var="section" value="${resume.getSection(sectionType)}"/>
            <jsp:useBean id="section" type="com.urise.webapp.model.sections.Section"/>
            <dl>
                <c:choose>

                    <c:when test="${sectionType == SectionType.PERSONAL || sectionType == SectionType.OBJECTIVE}">
                        <dl>
                            <dt>${sectionType.title}</dt>
                            <dd><textarea name="${sectionType}" rows=10 cols=100>${section}</textarea></dd>
                        </dl>
                    </c:when>

                    <c:when test="${sectionType == SectionType.ACHIEVEMENT || sectionType == SectionType.QUALIFICATIONS}">
                        <dl>
                            <dt>${sectionType.title}</dt>
                            <dd><textarea name="${sectionType}" rows=10
                                          cols=100><%=(String.join("\n", ((ListSection) section).getItems()))%></textarea>
                            </dd>
                        </dl>
                    </c:when>

                    <c:when test="${sectionType == SectionType.EXPERIENCE || sectionType == SectionType.EDUCATION}">

                        <c:forEach var="organization" items="<%=((OrganizationSection) section).getOrganizations()%>"
                                   varStatus="counter">
                            <dl>
                                <dt>Наименование:</dt>
                                <dd><input type="text" name='${sectionType}' size=100
                                           value="${organization.homePage.name}"></dd>
                            </dl>
                            <dl>
                                <dt>URL:</dt>
                                <dd><input type="text" name='${sectionType}link' size=100
                                           value="${organization.homePage.url}"></dd>
                                </dd>
                            </dl>

                            <c:forEach var="position" items="${organization.positions}">
                                <jsp:useBean id="position" type="com.urise.webapp.model.Organization.Position"/>
                                <dl>
                                    <dt>Дата начала:</dt>
                                    <dd>
                                        <input type="text" name="${sectionType}${counter.index}startDate" size=20
                                               value="<%=DateUtil.notNullDate(position.getStartDate())%>"
                                               placeholder="MM-yyyy">
                                    </dd>
                                </dl>
                                <dl>
                                    <dt>Дата окончания:</dt>
                                    <dd>
                                        <input type="text" name="${sectionType}${counter.index}endDate" size=20
                                               value="<%=DateUtil.notNullDate(position.getEndDate())%>"
                                               placeholder="MM-yyyy">
                                </dl>
                                <dl>
                                    <dt>Позиция:</dt>
                                    <dd><input type="text" name='${sectionType}${counter.index}position' size=100
                                               value="${position.title}">
                                </dl>
                                <dl>
                                    <dt>Обязанности:</dt>
                                    <dd><textarea name="${sectionType}${counter.index}desc" rows=10
                                                  cols=100>${position.description}</textarea></dd>
                                </dl>
                            </c:forEach>
                            <br>
                        </c:forEach>

                    </c:when>
                </c:choose>

            </dl>
        </c:forEach>
        <hr>

        <button type="submit">Сохранить</button>
        <button type="reset" onclick="window.history.back()">Отменить</button>
    </form>

</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>