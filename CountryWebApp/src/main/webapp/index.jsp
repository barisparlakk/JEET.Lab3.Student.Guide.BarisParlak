<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>CountryWeb &#128506;</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <link
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css"
        rel="stylesheet">
</head>

<body>
<h2 class="text-center mt-3">Get basic information about countries!</h2>

<div class="shadow p-3 mb-5 bg-body rounded container-sm">

    <!-- Search form -->
    <form action="countries" method="GET" class="mb-4">
        <label for="countryName">Enter Country Name:</label>
        <input type="text" id="countryName" name="name"
               class="form-control" placeholder="Enter FULL name or a fragment"
               required>
        <button type="submit" class="btn btn-danger mt-2">Search</button>
    </form>

    <!-- Error message -->
    <c:if test="${not empty errorMessage}">
        <div style="color: red;">
            <p><strong>Error: ${errorMessage}</strong></p>
        </div>
    </c:if>

    <!-- Results table -->
    <table class="table table-sm table-striped table-rounded-top table-bordered table-hover bg-primary table-light">
        <thead class="table-dark">
        <tr>
            <th>#</th>
            <th>Country Name</th>
            <th>Original Name</th>
            <th>Capital</th>
            <th>TLD</th>
            <th>Flag</th>
            <th>CoA</th>
            <th>Map</th>
        </tr>
        </thead>

        <tbody id="countryTableBody">

        <c:forEach var="country" items="${countryData}" varStatus="status">
            <tr>
                <td>${status.index + 1}</td>

                <!-- Country Name -->
                <td><c:out value="${country.name.common}" /></td>

                <!-- Original Name -->
                <td>
                    <c:choose>
                        <c:when test="${not empty country.name.nativeName}">
                            <c:forEach var="native" items="${country.name.nativeName}">
                                <c:out value="${native.value.official}" />
                            </c:forEach>
                        </c:when>
                        <c:otherwise>N/A</c:otherwise>
                    </c:choose>
                </td>

                <!-- Capital -->
                <td>
                    <c:choose>
                        <c:when test="${not empty country.capital}">
                            <c:out value="${country.capital[0]}" />
                        </c:when>
                        <c:otherwise>N/A</c:otherwise>
                    </c:choose>
                </td>

                <!-- TLD -->
                <td>
                    <c:choose>
                        <c:when test="${not empty country.tld}">
                            <c:out value="${country.tld[0]}" />
                        </c:when>
                        <c:otherwise>N/A</c:otherwise>
                    </c:choose>
                </td>

                <!-- Flag -->
                <td>
                    <img src="${country.flags.svg}"
                         alt="Flag of ${country.name.common}"
                         width="50" />
                </td>

                <!-- Coat of Arms -->
                <td>
                    <c:choose>
                        <c:when test="${not empty country.coatOfArms.svg}">
                            <img src="${country.coatOfArms.svg}"
                                 alt="Coat of Arms of ${country.name.common}"
                                 width="50" />
                        </c:when>
                        <c:otherwise>N/A</c:otherwise>
                    </c:choose>
                </td>

                <!-- Map -->
                <td>
                    <c:choose>
                        <c:when test="${not empty country.maps.openStreetMaps}">
                            <a href="${country.maps.openStreetMaps}" target="_blank">View Map</a>
                        </c:when>
                        <c:otherwise>N/A</c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>

        </tbody>
    </table>
</div>

</body>
</html>
