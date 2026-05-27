<%@page import="com.expedium.user.model.Filter"%>
<%@page import="com.expedium.user.service.UserService"%>
<%@page import="com.expedium.user.model.User"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%
UserService objService = new UserService();
    Integer iAdminKey = (Integer) session.getAttribute("adminkey");
    String sAction = request.getParameter("action");
    if (sAction == null) sAction = "";
    String sSortColumn=request.getParameter("sortColumn");
    String sSortOrder=request.getParameter("sortOrder");
    
    if (iAdminKey == null) {
        out.print("session expired");
        return;
    }
	
 
    // --- 2. PAGINATION & FILTER LOGIC ---
    int iCurrentPage = 1;
    int iPageSize = 10;
    try {
        if (request.getParameter("page") != null) iCurrentPage = Integer.parseInt(request.getParameter("page"));
        if (request.getParameter("pageSize") != null) iPageSize = Integer.parseInt(request.getParameter("pageSize"));
    } catch (Exception e) { iCurrentPage = 1; }
    

    List<User> objList;
    int iTotalUsers = 0;

    if ("filter".equals(sAction)) {
        int iOffset = (iCurrentPage - 1) * iPageSize;
        
        String sFirstName = request.getParameter("firstName");
        String sLastName = request.getParameter("lastName");
        String sEmail = request.getParameter("email");
        String sContact = request.getParameter("contact");
        String sGender = request.getParameter("gender");
        String sAddress = request.getParameter("address");
        
        String sFirstNameType = request.getParameter("firstNameType");
        String sLastNameType=request.getParameter("lastNameType");
        String sEmailType=request.getParameter("emailType");
        String sContactType=request.getParameter("contactType");
        String sGenderType=request.getParameter("genderType");
        String sAddressType=request.getParameter("addressType");
        
        sFirstName   = sFirstName != null ? sFirstName.trim() : "";
        sLastName   = sLastName != null ? sLastName.trim() : "";
        sEmail   = sEmail != null ? sEmail.trim() : "";
        sContact   = sContact != null ? sContact.trim() : "";
        sGender   = sGender != null ? sGender.trim() : "";
        sAddress   = sAddress!= null ? sAddress.trim() : "";
        sFirstNameType  = sFirstNameType != null ? sFirstNameType.trim() : "";
        sLastNameType   = sLastNameType != null ? sLastNameType.trim() : "";
        sEmailType = sEmailType != null ? sEmailType.trim() : "";
        sContactType   = sContactType != null ? sContactType.trim() : "";
        sGenderType   = sGenderType!= null ? sGenderType.trim() : "";
        sAddressType   = sAddressType!= null ? sAddressType.trim() : "";

        
        Filter  objFilter = new Filter();
        objFilter.setFirstName(sFirstName);
        objFilter.setLastName(sLastName);
        objFilter.setEmail(sEmail);
        objFilter.setContact(sContact);
        objFilter.setGender(sGender);
        objFilter.setAddress(sAddress);
        
        objFilter.setFirstNameType(sFirstNameType);
        objFilter.setLastNameType(sLastNameType);
        objFilter.setEmailType(sEmailType);
        objFilter.setContactType(sContactType);
        objFilter.setGenderType(sGenderType);
        objFilter.setAddressType(sAddressType);
      //  sFirstName   = sFirstName != null ? sFirstName.trim() : "";
//        obj.setsFirstName(sFirstName);
        // Pass cleaned strings to the count method
      //  totalUsers = objService.getFilteredCount(obj);
        
iTotalUsers = objService.getFilteredCount(
    iAdminKey,objFilter
);        
        // Pass original types to the filter method
        objList = objService.filterUsers(
            iAdminKey,
            objFilter,
            iPageSize,
            iOffset,
            sSortColumn, sSortOrder
        );
    }
else {

	int offset = (iCurrentPage - 1) * iPageSize;

	objList = objService.getUsersByAdmin(iAdminKey, offset, iPageSize,
		    sSortColumn,
		    sSortOrder);
	iTotalUsers = objService.getUserCount(iAdminKey);
    }
	

    
    int iTotalPages = (int) Math.ceil((double) iTotalUsers / iPageSize);
    int iStartRecord = (iTotalUsers == 0) ? 0 : (iCurrentPage - 1) * iPageSize + 1;
    int iEndRecord = Math.min(iCurrentPage * iPageSize, iTotalUsers);
%>

<!DOCTYPE html>
<html>
<head>
    <style>
    .modal {
    z-index: 2000 !important;
}

.modal-backdrop {
    z-index: 1990 !important;
}
.filter-section {
    margin-bottom: 5px; 
}
.table-wrapper {
    height: 360px;
    overflow-y: auto;
    overflow-x: hidden;
    scrollbar-gutter: stable;
    
}
.table-wrapper thead th {
    position: sticky;
    top: 0;
    z-index: 100;   
    background-color: #343a40;
    color: white;
    text-align: center;
}




.table {
  font-size: 0.85rem; 
  table-layout: fixed; 
}

.table th, .table td {
  text-align: center;
  vertical-align: middle;
  padding: 4px 6px; 
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
     padding: 2px 4px;
}

.table input[type="checkbox"] {
  transform: scale(0.8); /
}

.table .btn {
  font-size: 0.75rem;
  padding: 2px 6px;
}
.table tbody tr{
height:30px;
}
.table tbody tr:hover {
  background-color: #f1f1f1;
}
.sort-container {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 5px;
}

.sort-icons {
    display: flex;
    flex-direction: column;  
    line-height: 10px;
}

.sort-icons i {
    font-size: 10px;
    cursor: pointer;
}

.sort-icons i:hover {
    color: #bbbbbb;
}
.active-sort {
    color:#007bff !important ;  
}
.sort-icons i.active-sort {
    color: #007bff !important;
}
.disabled-sort {
    pointer-events: none;
    opacity: 0.5;
    cursor: default !important;
}
    .pagination-container {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-top: 8px;
    margin-bottom: 10px;
    font-family: Arial, sans-serif;
    position: relative;
}
/* .pagination-container.d-none {
    display: none !important;
}
 */
.pagination-center {
    display: flex;
    gap: 5px;
    align-items: center;
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
}
        .page-number { cursor: pointer; padding: 6px 10px; border-radius: 4px; border: 1px solid #ddd; }
        .page-number.active { background-color: #2ecc71; color: white; border-color: #2ecc71; font-weight: bold; }
        .page-arrow { cursor: pointer; font-weight: bold; padding: 0 10px; }
        .d-none { display: none; }
  .sort-container span {
    cursor: pointer;
}
 <link rel="stylesheet" href="css/bootstrap-icons.min.css">
 <script src="javascript/user.js"></script>
    </style>
</head>
<%
boolean bDisableSorting = (objList == null || objList.isEmpty());
String sDisableClass = bDisableSorting ? "disabled-sort" : "";
String sDisableIcon = bDisableSorting ? "d-none" : "";
 %>
<body>

<div id="userTableWrapper" class="table-wrapper">
<div id="globalLoader" class="loader-overlay">
    <div class="loader"></div>
</div> 
<table class="table table-bordered mt-3">
    <thead class="table-dark">
        <tr>
   			<th style="width: 40px;"><input type="checkbox" id="selectToDelete"></th>
   			<th style="width: 50px;">Sl No</th>
           <th>
    <div class="sort-container">
        <span class="<%= sDisableClass %>"onclick="sortUsers('firstname')">Firstname</span>
        <div class="sort-icons">
            <i class="bi bi-caret-up-fill <%= sDisableIcon %>" onclick="sortUsers('firstname','asc')"></i>
            <i class="bi bi-caret-down-fill <%= sDisableIcon %>" onclick="sortUsers('firstname','desc')"></i>
        </div>
    </div>
</th>           
    <th>
    <div class="sort-container">
        <span class="<%= sDisableClass %>"onclick="sortUsers('middlename')">Middlename</span>
        <div class="sort-icons">
            <i class="bi bi-caret-up-fill <%= sDisableIcon %>" onclick="sortUsers('middlename','asc')"></i>
            <i class="bi bi-caret-down-fill <%= sDisableIcon %>" onclick="sortUsers('middlename','desc')"></i>
        </div>
    </div>
</th>    
    <th>
    <div class="sort-container">
        <span class="<%= sDisableClass %>" onclick="sortUsers('lastname')">lastname</span>
        <div class="sort-icons">
            <i class="bi bi-caret-up-fill <%= sDisableIcon %>" onclick="sortUsers('lastname','asc')"></i>
            <i class="bi bi-caret-down-fill <%= sDisableIcon %>" onclick="sortUsers('lastname','desc')"></i>
        </div>
    </div>
</th> 
    <th style="width:170px;">
    <div class="sort-container">
        <span class="<%= sDisableClass %>"onclick="sortUsers('email')">Email</span>
        <div class="sort-icons">
            <i class="bi bi-caret-up-fill <%= sDisableIcon %>" onclick="sortUsers('email','asc')"></i>
            <i class="bi bi-caret-down-fill <%= sDisableIcon %>" onclick="sortUsers('email','desc')"></i>
        </div>
    </div>
</th> 
            <th>Contact</th>
            <th>Gender</th>
            <th>DOB</th>
<th>
    <div class="sort-container">
        <span class="<%= sDisableClass %> "onclick="sortUsers('address')">Address</span>
        <div class="sort-icons">
            <i class="bi bi-caret-up-fill <%= sDisableIcon %>" onclick="sortUsers('address','asc')"></i>
            <i class="bi bi-caret-down-fill <%= sDisableIcon %>" onclick="sortUsers('address','desc')"></i>
        </div>
    </div>
</th>             <th>Edit</th>
				  <th>Delete</th>
				  <th>PDF</th>
        </tr>
    </thead>
    <tbody>
    
<%
boolean bIsFilter = "filter".equals(sAction);

if (objList != null && !objList.isEmpty()) {
	int iRowCount = (iCurrentPage - 1) * iPageSize;

    for (User objUser : objList) {
        iRowCount++;
%>
        <tr>
    <td style="width: 40px;">
        <input type="checkbox" class="userCheckbox" value="<%=objUser.getUserKey()%>">
    </td>

    <td style="width: 50px;">
        <%= iRowCount %>
    </td>
            <td><%=objUser.getUserFirstname()%></td>
            <td><%=objUser.getUserMiddlename()%></td>
            <td><%=objUser.getUserLastname()%></td>
            <td><%=objUser.getUserEmail()%></td>
            <td><%=objUser.getUserContact()%></td>
            <td><%=objUser.getUserGender()%></td>
            <td><%=objUser.getUserdob()%></td>
            <td><%=objUser.getUserAddress()%></td>
            <td><button class="btn btn-primary" onclick="editUser(this)">Edit</button></td>
			<td><i class="bi bi-trash-fill text-danger"
       style="cursor:pointer; font-size:16px;"
       onclick="deleteSingleUser(this)">
    </i></td>
    <td>
    <button class="btn btn-outline-primary btn-sm"
    onclick="downloadPdf('<%=objUser.getUserEmail()%>')">
    <i class="bi bi-download"></i>
</button>
</td>
            
        </tr>
<%
    }
    int iActualRows = objList.size();
    for (int iI = iActualRows; iI < 10; iI++) {
    	out.print("<tr>");
    	for(int iJ = 0; iJ < 13; iJ++) {
    	    out.print("<td>&nbsp;</td>");
    	}
    	out.print("</tr>");    }

} else {
    if (bIsFilter) {
%>
        <tr style="height: 300px;">
    <td colspan="13" class="text-center align-middle text-danger fw-bold" style="font-size:20px;">
        No users found
    </td>
</tr>
<%
    } else {
        for (int iI = 0; iI < iPageSize; iI++) {
        	out.print("<tr class='empty-row'><td colspan='13'></td></tr>");        }
    }
}
%>
</tbody>
</table>

</div>

<script>

//this one is for deleting by using checkbox
$(document).on('change', '#selectToDelete', function() {
    const checked = $(this).is(':checked');
    $('.userCheckbox').prop('checked', checked);
});

//if checkbox length =checkboxes checked  then all checked
$(document).on('change', '.userCheckbox', function() {
    const allChecked = $('.userCheckbox').length === $('.userCheckbox:checked').length;
    $('#selectToDelete').prop('checked', allChecked); 
});
//this one is used to restrict backward forward navigation after logout
window.addEventListener("pageshow", function (event) {

    let loggedIn = "<%= session.getAttribute("adminkey") %>";
    if (!loggedIn || event.persisted || performance.getEntriesByType("navigation")[0].type === "back_forward") {
        window.location.replace("Login.html");
    }
});

</script>
<%
boolean hidePagination = (objList == null || objList.isEmpty());
%>
<%if(!hidePagination){ %>
<div class="pagination-container <%= sDisableClass %>">
<input type="hidden" id="totalPages" value="<%= iTotalPages %>">
    <div class="pagination-left">
        Total: <%= iTotalUsers %> | Showing <%= iStartRecord %>-<%= iEndRecord %>
    </div>
    
    <div class="pagination-center">
        <span class="page-arrow prev" data-page="<%= iCurrentPage - 1 %>">&lt;</span>
        <%
            int iStart = Math.max(1, iCurrentPage - 1);
            int iEnd = Math.min(iTotalPages, iCurrentPage + 1);
            if(iCurrentPage > 2) { %>
             <span class="page-number" data-page="1">1</span> <span>...</span> <% 
             }
            for(int iI = iStart; iI <= iEnd; iI++) { %>
                <span class="page-number <%= (iI==iCurrentPage)?"active":"" %>" data-page="<%= iI %>"><%= iI %></span>
            <% }

            if(iCurrentPage < iTotalPages - 1) { %> 
            <span>...</span> <span class="page-number" data-page="<%= iTotalPages %>"><%= iTotalPages %></span> <% 
            }
        %>
        <span class="page-arrow next" data-page="<%= iCurrentPage + 1 %>">&gt;</span>
    </div>
    
    <div class="pagination-right">Records per page
        <select id="pageSizeSelect">
            <option value="10" <%= iPageSize==10?"selected":"" %>>10</option>
            <option value="20" <%= iPageSize==20?"selected":"" %>>20</option>
            <option value="30" <%= iPageSize==30?"selected":"" %>>30</option>
            <option value="100" <%= iPageSize==100?"selected":"" %>>100</option>
        </select>
        <input type="text" id="goToPage" style="width: 65px; text-align:center " min="1" max="<%= iTotalPages %>">
        <button id="goBtn" class="btn btn-sm btn-secondary">Go</button>
        <span id="pageError" style="color:red; font-weight:bold; display:block;margin-left:65px;"></span>
        
    </div>
    
</div>
<%} %>
</body>
</html>