<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Dashboard</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/Login.css" rel="stylesheet">
<link rel="stylesheet" href="css/bootstrap-icons.css">
<script src="JS/jquery-3.6.0.min.js"></script>
	
<%
if (session == null || session.getAttribute("adminkey") == null) {
    response.sendRedirect("Login.html");
    return;
}
%>
<link href="css/bootstrap-icons.css" rel="stylesheet">
<style>

body {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
}

main {
    flex: 1;
}
header {
    position: sticky;
    top: 0;
    z-index: 1000;
}

.navbar {
    margin: 0 !important;
}
.navbar {
    padding-top: 5px;
    padding-bottom: 5px;
}

.navbar-brand {
    font-size: 16px;
}

.navbar .nav-link {
    font-size: 14px;
    padding: 4px 8px;
}
.nav-clock {
	color: #fff;
	font-size: 15px;
	font-weight: 500;
	padding: 6px 12px;
	border-radius: 6px;
	background: rgba(255, 255, 255, 0.15);
}
.filter-select {
    width: 110px;
     background-color: #f5f5f5;  
    border: 1px solid #ccc;    
    color: #333;                
    border-radius: 4px;
}

.filter-group {
    display: flex;
    align-items: center;
    gap: 8px;
    flex-wrap: nowrap;
}

.filter-label {
    font-weight: 600;
    white-space: nowrap;
    min-width: 90px;
      color: #2c2c2c;
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI",
                 Roboto, Arial, Helvetica, sans-serif;
    font-size: 15px;
}

.filter-input {
    width: 200px;
    height: 34px;
    font-size: 14px;
}

.filter-select {
    width: 140px;
    height: 34px;
    font-size: 14px;
}

.filter-buttons .btn {
    padding: 0.25rem 0.6rem;
    font-size: 13px;
    height: 30px;
}
.filter-buttons {
    gap: 4px;
}
.password-box {
    width: 100%;
    max-width: 350px;   
}
.modal-md-custom {
    max-width: 420px;   
    width: 100%;
}
.input-group .form-control {
    border-radius: 6px !important;
}


.input-group .input-group-text {
    margin-left: 5px;
    }
    .password-toggle {
    cursor: pointer;
    user-select: none;
}

.password-wrapper {
    position: relative;
}

.password-input {
    padding-right: 45px;
}

.password-eye {
    position: absolute;
    right: 15px;
    top: 32px;
    cursor: pointer;
    font-size: 18px;
    color: #555;
}
.error{
    display:none;
    color:red;
    font-size:14px;
    margin-top:2px;
}

</style>


</head>
<body>
	
	<header>
		<nav class="navbar sticky-top navbar-expand-lg navbar-dark"
     style="background: linear-gradient(90deg, #1f1f3a, #2e2e5e); top:0; z-index:1000;">
			<div class="container-fluid">
<a class="navbar-brand" href="#">Welcome,<b> <span id="navName"></span></b></a>
				<button class="navbar-toggler" type="button"
					data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown"
					aria-controls="navbarNavDropdown" aria-expanded="false"
					aria-label="Toggle navigation">
					<span class="navbar-toggler-icon"></span>
				</button>

				<div class="collapse navbar-collapse" id="navbarNavDropdown">
					<ul class="navbar-nav ms-auto">

						<li class="nav-item d-flex align-items-center me-3"><span
							id="liveTime" class="nav-clock"></span></li>

						<li class="nav-item dropdown"><a
							class="nav-link dropdown-toggle d-flex flex-column align-items-center"
							href="#" role="button" data-bs-toggle="dropdown"
							aria-expanded="false"> <img src="img/user.png" alt="Profile"
								class="mb-1"
								style="width: 30px; height: 30px; object-fit: cover;"> <span>Profile</span>
						</a>
							<ul class="dropdown-menu dropdown-menu-end">
								<li><a class="dropdown-item" href="#"  
									data-bs-toggle="modal" data-bs-target="#profileModal">My
										Profile</a></li><!-- href=# because we are not redirecting to another page we are just opening modal -->
								<li><a class="dropdown-item" href="#" 
									data-bs-toggle="modal" data-bs-target="#changePasswordModal">Change
										password</a></li>
								<li><hr class="dropdown-divider"></li>
								<li><a class="dropdown-item" href="#" id="logoutBtn" onclick="logoutBtn(event);">Logout</a></li>
							</ul></li>
					</ul>
				</div>
			</div>
		</nav>
	</header>
	<div class="modal fade" id="profileModal" tabindex="-1" data-bs-backdrop="static" data-bs-keyboard="false"
		aria-labelledby="profileModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-xl modal-dialog-centered">
			<div class="modal-content">
				<div class="modal-header w-100 text-center" style="background: #0099FF;">

					<h5 class="modal-title w-100 text-center">My Profile</h5>

					<div class="ms-auto d-flex align-items-center">
						<button type="button" id="editBtn"
							class="btn btn-sm me-2 edit-btn-style" onclick="editAdminBtn(this);">
							<i class="bi bi-pencil-square"></i>
						</button>

						<button type="button" class="btn-close btn-close-white"
							data-bs-dismiss="modal"></button>

					</div>

				</div>
				<!-- --------------edit profile--------------- -->
				<div class="modal-body"  data-bs-backdrop="static"  data-bs-keyboard="false">

					<form id="profileForm" class="form2" autocomplete="off">

						<div class="row mb-3">
							<div class="col-sm-4">
								<label for="reg_adminid">Admin ID<span style="color: red; font-weight: bold;">*</span></label> <input type="text"
									id="reg_adminid" name="reg_adminid" class="form-control"
									value="<%=session.getAttribute("adminid")%>" readonly>
							</div>
							<div class="col-sm-4">
								<label for="firstname">First Name<span style="color: red; font-weight: bold;">*</span></label> 
								<input type="text" id="firstname" class="form-control editField" readonly>
								<span id="firstname-error"  class="error" style="display:none;color:red;"></span>
								
								
							</div>
							<div class="col-sm-4">
								<label for="middlename">Middle Name</label> 
								<input type="text" id="middlename" class="form-control editField" readonly>
								
							</div>
						</div>
						<div class="row mb-3">
							<div class="col-sm-4">
								<label for="lastname">Last name<span style="color: red; font-weight: bold;">*</span></label> 
								<input type="text" id="lastname" class="form-control editField" readonly>
								<span id="lastname-error"  class="error" style="display:none;color:red;"></span>
								
								
							</div>
							<div class="col-sm-4">
								<label for="email">Email<span style="color: red; font-weight: bold;">*</span></label> 
								<input type="text" id="email" class="form-control editField" readonly>
								<span id="email-error"  class="error" style="display:none;color:red;"></span>
								
								
							</div>
							<div class="col-sm-4">
								<label for="address">Address<span style="color: red; font-weight: bold;">*</span></label> 
								<input type="text" id="address" class="form-control editField" readonly>
								<span id="address-error"  class="error" style="display:none;color:red;"></span>
								
								
							</div>
						</div>
						<div class="row mb-3">

							<div class="col-sm-4">
								<label for="contact">Contact<span style="color: red; font-weight: bold;">*</span></label> 
								<input type="tel" id="contact" class="form-control editField" readonly>
                                <span id="contact-length-error"  class="error" style="display:none;color:red;"></span>
                                 

							</div>
							<div class="col-sm-4">
								<label for="gender">Gender<span style="color: red; font-weight: bold;">*</span></label> 
                         <select id="gender" class="form-select editField" disabled>
                                  <option value="" disabled selected hidden>Select Gender</option>
                                  <option value="male">Male</option>
                                  <option value="female">Female</option>
                                  <option value="others">Others</option>
                         </select>
							</div>

							<div class="col-sm-4">
								<label for="dob">DOB<span style="color: red; font-weight: bold;">*</span></label> <input type="date" id="dob" class="form-control editField" readonly> 
								<span id="dob-error" class="error"style="display: none; color: red;"></span>
						    </div>

						</div>
						<div class="modal-footer d-none" id="profileFooter">

							<button type="button" class="btn btn-secondary"
								data-bs-dismiss="modal">Cancel</button>
							<button id="updateProfileButton" type="submit"
								class="btn btn-primary" form="profileForm" onclick="updateAdminProfile(event);">Update</button>
						</div>
					</form>

				</div>
			</div>
		</div>
	</div>
	<!-- --------------Change password modal--------------------- -->
<div class="modal fade" id="changePasswordModal" tabindex="-1"
     data-bs-backdrop="static" data-bs-keyboard="false">

    <div class="modal-dialog modal-dialog-centered modal-md-custom">

        <div class="modal-content shadow">

            <div class="modal-header bg-dark text-white">
                <h5 class="modal-title w-100 text-center m-0">Change Password</h5>

                <button type="button"
                        class="btn-close btn-close-white"
                        data-bs-dismiss="modal"></button>
            </div>

            <div class="modal-body d-flex justify-content-center">

                <div class="password-box w-100">

                    <!-- Current Password -->
                    <div class="mb-3 password-wrapper">

                        <label>
                            Current Password
                            <span style="color:red;font-weight:bold;">*</span>
                        </label>

                        <input type="password"
                               id="currentPassword"
                               class="form-control password-input">

                        <i class="bi bi-eye-fill password-eye"
                           onclick="togglePassword('currentPassword', this)"></i>

                        <span id="currentPasswordError"
                              class="error"
                              style="display:block;color:red;"></span>
                    </div>

                    <div class="mb-3 password-wrapper">

                        <label>
                            New Password
                            <span style="color:red;font-weight:bold;">*</span>
                        </label>

                        <input type="password"
                               id="newPassword"
                               class="form-control password-input">

                        <i class="bi bi-eye-fill password-eye"
                           onclick="togglePassword('newPassword', this)"></i>

                        <span id="newPasswordError"
                              class="error"
                              style="display:block;color:red;"></span>
                    </div>

                    <div class="mb-3 password-wrapper">

                        <label>
                            Confirm Password
                            <span style="color:red;font-weight:bold;">*</span>
                        </label>

                        <input type="password"
                               id="confirmPassword"
                               class="form-control password-input">

                        <i class="bi bi-eye-fill password-eye"
                           onclick="togglePassword('confirmPassword', this)"></i>

                        <span id="confirmPasswordError"
                              class="error"
                              style="display:block;color:red;"></span>
                    </div>

                </div>
            </div>

            <div class="modal-footer">

                <button type="button"
                        class="btn btn-secondary"
                        onclick="clearPassword()">
                    Clear
                </button>

                <button type="button"
                        id="updatePasswordBtn"
                        class="btn btn-primary"
                        onclick="updateUserPassword()">
                    Update Password
                </button>

            </div>

        </div>
    </div>
</div>

<!-- /** Fiters in dashboard**/ -->
<div class="filter-wrapper">
<div class="container mt-3">
  <div class="card shadow-sm p-3">

  

    <div class="row gx-3 gy-2">

      <div class="col-md-4 d-flex align-items-center filter-group">

    <label class="filter-label">First Name:</label>

    <select id="firstnameType" class="form-select filter-select">
        <option value="contains">Contains</option>
        <option value="starts">Starts with</option>
        <option value="ends">Ends with</option>
        <option value="equals">Equals</option>
    </select>

    <input type="text" id="first-name-search" class="form-control filter-input" placeholder="First Name">
</div>

     
      <div class="col-md-4 d-flex align-items-center filter-group">
      <label class="filter-label">Last Name:</label>
      <select id="lastnameType" class="form-select filter-select" >
          <option value="contains">Contains</option>
          <option value="starts">Starts with</option>
          <option value="ends">Ends with</option>
          <option value="equals">Equals</option>
        </select>
        <input type="text" id="last-name-search" class="form-control filter-input" placeholder="Last Name">
        
      </div>

   
      <div class="col-md-4 d-flex align-items-center filter-group">
      <label class="filter-label">Email:</label>
      <select id="emailType" class="form-select filter-select">
          <option value="contains">Contains</option>
          <option value="starts">Starts with</option>
          <option value="ends">Ends with</option>
          <option value="equals">Equals</option>
        </select>
        <input type="text" id="email-search" class="form-control filter-input" placeholder="Email">
        
        
      </div>

      
      <div class="col-md-4 d-flex align-items-center filter-group">
            <label class="filter-label">Contact:</label>
      
      <select id="contactType" class="form-select filter-select ">
          <option value="contains">Contains</option>
          <option value="starts">Starts with</option>
          <option value="ends">Ends with</option>
          <option value="equals">Equals</option>
        </select>
        <input type="text" id="contact-search" class="form-control filter-input" placeholder="Contact">
        
      </div>

     
     <div class="col-md-4 d-flex align-items-center filter-group">

    <label class="filter-label">Gender:</label>

    
        <input style="width:117px;"type="text"
       id="genderType"
       class="form-control filter-select"
       value="Equals"
       readonly >


    <select id="gender-search" class="form-select filter-input">
        <option value="" disabled selected hidden>Select Gender</option>
        <option value="Male">Male</option>
        <option value="Female">Female</option>
        <option value="Others">Others</option>
    </select>

</div>


      <div class="col-md-4 d-flex align-items-center filter-group">
            <label class="filter-label">Address:</label>
      
      <select id="addressType" class="form-select filter-select">
          <option value="contains">Contains</option>
          <option value="starts">Starts with</option>
          <option value="ends">Ends with</option>
          <option value="equals">Equals</option>
        </select>
        <input type="text" id="address-search" class="form-control filter-input" placeholder="Address">
        
      </div>

    </div>

   <div class="mt-3 d-flex justify-content-end gap-2 filter-buttons">
    	<button id="clearFilterBtn"class="btn btn-outline-secondary" onclick="clearFilters()">Clear</button>
        <button class="btn btn-primary" id="searchBtn" onclick="filterUser()">Search</button>
      
    </div>

  </div>
</div>
</div>

	<main class="container mb-4">
		<div id="tableContainer" ></div>
		
		
		<!-- EDIT USER MODAL -->
<div class="modal fade" id="editUserModal" tabindex="-1" data-bs-backdrop="static" data-bs-keyboard="false">
  <div class="modal-dialog modal-dialog-centered" style="max-width: 800px;">
    <div class="modal-content">

      <div class="modal-header">
        <h5 class="modal-title w-100 text-center m-0">Edit User</h5>
        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
      </div>

      <div class="modal-body">

        <input type="hidden" id="editUserKey">

        <div class="row">
          <div class="col-md-4 mb-2">
            <label>First Name<span style="color: red; font-weight: bold;">*</span></label>
            <input type="text" id="editFirstname" class="form-control">
            <span id="editFirstNameError"class="error text-danger" style="display:none;"></span>
          </div>

          <div class="col-md-4 mb-2">
            <label>Middle Name</label>
            <input type="text" id="editMiddlename" class="form-control">
          </div>

          <div class="col-md-4 mb-2">
            <label>Last Name<span style="color: red; font-weight: bold;">*</span></label>
            <input type="text" id="editLastname" class="form-control">
            <span id="editLastNameError" class="error text-danger" style="display:none;"></span>
          </div>
        </div>

        <div class="row">
          <div class="col-md-4 mb-2">
            <label>Email:<span style="color: red; font-weight: bold;">*</span></label>
            <input type="email" id="editEmail" class="form-control">
            <span id="editEmailError" class="error text-danger" style="display:none;"></span>
          </div>

          <div class="col-md-4 mb-2">
            <label>Contact<span style="color: red; font-weight: bold;">*</span></label>
            <input type="text" id="editContact" class="form-control">
            <span id="editContactError" class="error text-danger" style="display:none;"></span>
          </div>

          <div class="col-md-4 mb-2">
            <label>Gender<span style="color: red; font-weight: bold;">*</span></label>
            <select id="editGender" class="form-control">
              <option value="">Select Gender</option>
              <option>Male</option>
              <option>Female</option>
              <option>Others</option>
            </select>
            <span id="editGenderError" class="error text-danger" style="display:none;"></span>
          </div>
        </div>

        <div class="row">
          <div class="col-md-6 mb-2">
            <label>Date of Birth<span style="color: red; font-weight: bold;">*</span></label>
            <input type="date" id="editDob" class="form-control">
            <span id="editDobError" class="error text-danger" style="display:none;"></span>
          </div>

          <div class="col-md-6 mb-2">
            <label>Address<span style="color: red; font-weight: bold;">*</span></label>
            <textarea id="editAddress" class="form-control"></textarea>
            <span id="editAddressError" class="error text-danger" style="display:none;"></span>
          </div>
        </div>

      </div>

      <div class="modal-footer">
        <button class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
        <button class="btn btn-primary" id="updateUserBtn" onclick="updateUser()">Update</button>
      </div>

    </div>
  </div>
</div>
		
	
	<div class="d-flex justify-content-center gap-2" style="margin-top:10px;">
    <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addUserModal">
        Add 
    </button>

    <button class="btn btn-danger" id="deleteBtn" onclick="deleteUser()">
        Delete
    </button>
</div>


<div class="modal fade" id="addUserModal" tabindex="-1" data-bs-backdrop="static" data-bs-keyboard="false" >
  <div class="modal-dialog modal-lg modal-dialog-centered" style="border-color: white;">
    <div class="modal-content">

      <div class="modal-header ">
    <h5 class="modal-title w-100 text-center m-0">Add User</h5>
    <button type="button" class="btn-close btn-close-white"
        onclick="clearAddUserForm()" data-bs-dismiss="modal"></button>
</div>

<div class="modal-body">
    <form id="addUserForm">

    <div id="successMsg" class="text-success"
        style="display:none;margin-bottom:10px;"></div>

    <div class="row mb-3">

        <div class="col-md-4">
            <label>First Name<span style="color: red; font-weight: bold;">*</span></label>
            <input type="text" id="firstName" class="form-control">
            <span id="first-name-error" class="error" style="display:none;color:red;"></span>
        </div>

        <div class="col-md-4">
            <label>Middle Name</label>
            <input type="text" id="middleName" class="form-control">
        </div>

        <div class="col-md-4">
            <label>Last Name<span style="color: red; font-weight: bold;">*</span></label>
            <input type="text" id="lastName" class="form-control">
            <span id="last-name-error" class="error" style="display:none;color:red;"></span>
        </div>

    </div>

    <div class="row mb-3">

        <div class="col-md-4">
            <label>Email<span style="color: red; font-weight: bold;">*</span></label>
            <input type="email" id="Email" class="form-control">
<span id="add-email-error" class="error" style="display:none;color:red;"></span>
        </div>

        <div class="col-md-4">
            <label>Contact Number<span style="color: red; font-weight: bold;">*</span></label>
            <input type="text" id="Contact" class="form-control">
            <span id="contact-number-error" class="error" style="display:none;color:red;"></span>
        </div>

        <div class="col-md-4">
            <label>Gender<span style="color: red; font-weight: bold;">*</span></label>
            <select id="Gender" class="form-control">
                <option value="">Select Gender</option>
                <option>Male</option>
                <option>Female</option>
                <option>Others</option>
            </select>
            <span id="gender-error" class="error" style="display:none;color:red;"></span>
        </div>

    </div>

    <div class="row mb-3">

        <div class="col-md-6">
            <label>Date of Birth<span style="color: red; font-weight: bold;">*</span></label>
            <input type="date" id="Dob" class="form-control">
<span id="add-dob-error" class="error" style="display:none;color:red;"></span>
        </div>

        <div class="col-md-6">
            <label>Address<span style="color: red; font-weight: bold;">*</span></label>
            <textarea id="Address" class="form-control"></textarea>
<span id="add-address-error" class="error" style="display:none;color:red;"></span>
        </div>

    </div>

</form>
</div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" onclick="clearAddUserForm()">Clear</button>
        <button type="button" class="btn btn-success"  id="saveUserBtn" onclick="addUser()">Save</button>
      </div>

    </div>
  </div>
</div>
	</main>

	<footer class="mt-auto py-3"
		style="background: #191954; text-align: center; color: grey;">
		<p>&copy; 2026 expEDIum</p>
	</footer>

	<div id="loginLoader" class="fullscreen-loader d-none">
    <div class="loader"></div>
</div>
    <script src="javascript/Login.js"></script>
	<script src="javascript/loader.js"></script>
    <script src="javascript/user.js"></script>
	<script src="javascript/sweetalert2@11.js"></script>
	<script src="javascript/bootstrap.bundle.min.js"></script>
	
</body>
</html>