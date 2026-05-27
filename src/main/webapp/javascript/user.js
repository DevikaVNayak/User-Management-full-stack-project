
var currentPage = 1;
var pageSize = 10;
var isFilterActive = false;

let sortColumn = "";
let sortOrder = "asc";
let activeFilters = null;
let lastFilterRequest = "";


function sortUsers(column, order = null) {


	if (order) {
		sortColumn = column;
		sortOrder = order;
	} else {
		
		if (sortColumn === column) {
			sortOrder = (sortOrder === "asc") ? "desc" : "asc";
		} else {
			sortColumn = column;
			sortOrder = "asc";
		}
	}

	handleUsers(currentPage);
}

function applySortHighlight() {
	if (!sortColumn || !sortOrder)
		return;

	$(".sort-icons i").removeClass("active-sort");

	$(".sort-icons i").each(function() {
		let onclickAttr = $(this).attr("onclick");

		if (onclickAttr.includes(sortColumn) && onclickAttr.includes(sortOrder)) {
			$(this).addClass("active-sort");
		}
	});
}

// =============== ADD USER ==================
function addUser() {


	$(".error").hide().text("");
	$("#successMsg").hide();


	let firstName = $("#firstName").val().trim();
	let middleName = $("#middleName").val().trim();
	let lastName = $("#lastName").val().trim();
	let email = $("#Email").val().trim();
	let contact = $("#Contact").val().trim();
	let gender = $("#Gender").val();
	let dob = $("#Dob").val();
	let address = $("#Address").val().trim();
	let today = new Date();
	today.setHours(0, 0, 0, 0);
	let selectedDob = new Date(dob);

	let isValid = true;

	if (firstName === "") {

    $("#first-name-error")
        .text("First name required")
        .show();

    isValid = false;

} else if (!/^[A-Za-z]+$/.test(firstName)) {

    $("#first-name-error")
        .text("Only alphabets allowed")
        .show();

    isValid = false;
}


if (lastName === "") {

    $("#last-name-error")
        .text("Last name required")
        .show();

    isValid = false;

} else if (!/^[A-Za-z]+$/.test(lastName)) {

    $("#last-name-error")
        .text("Only alphabets allowed")
        .show();

    isValid = false;
}


if (email === "") {

    $("#add-email-error")
        .text("Email required")
        .show();

    isValid = false;

} else if (!/^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/.test(email)) {

    $("#add-email-error")
        .text("Invalid email")
        .show();

    isValid = false;
}


if (contact === "") {

    $("#contact-number-error")
        .text("Contact number required")
        .show();

    isValid = false;

} else if (!/^[0-9]{10}$/.test(contact)) {

    $("#contact-number-error")
        .text("Enter valid 10-digit number")
        .show();

    isValid = false;
}


if (!gender) {

    $("#gender-error")
        .text("Please select gender")
        .show();

    isValid = false;
}


if (!dob) {

    $("#add-dob-error")
        .text("DOB required")
        .show();

    isValid = false;

} else if (selectedDob >= today) {

    $("#add-dob-error")
        .text("Current date and future dates are not allowed")
        .show();

    isValid = false;
}


if (address === "") {

    $("#add-address-error")
        .text("Address required")
        .show();

    isValid = false;
}


	if (!isValid){
		return;
}
	$.ajax({
		url: "UserController",
		type: "POST",
		data: {
			action: "add",
			firstName: firstName,
			middleName: middleName,
			lastName: lastName,
			email: email,
			contact: contact,
			gender: gender,
			dob: dob,
			address: address
		},
		success: function(response) {
    let res = response.trim().toLowerCase();

    if (res === "session_expired") {
        window.location.href = "SessionExpired.jsp";
        return;
    }

    if (res === "success") {
        $(".error").hide().text("");
        $("#addUserModal").modal('hide');
        Swal.fire({
            icon: 'success',
            title: 'Success',
            text: 'User added successfully',
            timer: 500,
            showConfirmButton: false
        }).then(() => {
            $("#addUserForm")[0].reset();
            handleUsers(currentPage);
        });
    } else {
        if (res.includes("invalid_email"))
            $("#add-email-error").text("Invalid email address").show();
        if (res.includes("email already exists"))
            $("#add-email-error").text("Email already exists").show();
        if (res.includes("contact"))
            $("#contact-number-error").text("Invalid contact number").show();
        if (res.includes("gender"))
            $("#gender-error").text("Please select gender").show();
    }
},
		error: function() {
			Swal.fire({
				icon: 'error',
				title: 'Error',
				text: 'Server error, try again'
			});
		}
	});
}

// =============== EDIT USER ==================
function editUser(button) {
	let row = $(button).closest("tr");

	$("#editUserKey").val(row.find(".userCheckbox").val());
	$("#editFirstname").val(row.find("td:eq(2)").text().trim());
	$("#editMiddlename").val(row.find("td:eq(3)").text().trim());
	$("#editLastname").val(row.find("td:eq(4)").text().trim());
	$("#editEmail").val(row.find("td:eq(5)").text().trim());
	$("#editContact").val(row.find("td:eq(6)").text().trim());
	$("#editGender").val(row.find("td:eq(7)").text().trim());
	$("#editDob").val(row.find("td:eq(8)").text().trim());
	$("#editAddress").val(row.find("td:eq(9)").text().trim());

	let modal = new bootstrap.Modal(document.getElementById('editUserModal'));
	modal.show();
}

// =============== UPDATE USER ==================
function updateUser() {

	let isValid = true;
	$(".error").hide();

	let contact = $("#editContact").val().trim();
	let email = $("#editEmail").val().trim();
	let dob = $("#editDob").val().trim();
	let firstName = $("#editFirstname").val().trim();
	let lastName = $("#editLastname").val().trim();
	let address = $("#editAddress").val().trim();
	let gender = $("#editGender").val().trim();
   let today = new Date();
today.setHours(0,0,0,0);

let selectedDob = new Date(dob);

	if (firstName === "" || !/^[A-Za-z]+$/.test(firstName)) {
		$("#editFirstNameError").text("Valid first name required").show();
		isValid = false;
	}

	if (lastName === "" || !/^[A-Za-z]+$/.test(lastName)) {
		$("#editLastNameError").text("Valid last name required").show();
		isValid = false;
	}

	if (!dob) {

		$("#editDobError").text("DOB required").show();
		isValid = false;

	} else if (selectedDob  >= today) {

		$("#editDobError")
			.text("Current date and future dates are not allowed")
			.show();

		isValid = false;
	}

	if (address === "") {
		$("#editAddressError").text("Address required").show();
		isValid = false;
	}

	if (!gender) {
		$("#editGenderError").text("Gender required").show();
		isValid = false;
	}

	if (contact === "" || !/^[0-9]{10}$/.test(contact)) {

		$("#editContactError")
			.text("Enter valid 10-digit number")
			.show();

		isValid = false;
	}

	if (email === "" || !/^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/.test(email)) {

		$("#editEmailError")
			.text("Invalid email")
			.show();

		isValid = false;
	}

	if (!isValid) {
		return;
	}

	$.ajax({
		url: "UserController",
		type: "POST",
		data: {
			action: "update",
			userKey: $("#editUserKey").val(),
			firstName: firstName,
			middleName: $("#editMiddlename").val(),
			lastName: $("#editLastname").val(),
			email: email,
			contact: contact,
			gender: gender,
			dob: dob,
			address: address
		},
		success: function(res) {

			res = res.trim().toLowerCase();

			if (res === "session_expired") {
				window.location.href = "SessionExpired.jsp";
				return;
			}

			if (res.includes("success")) {

				bootstrap.Modal
					.getInstance(document.getElementById('editUserModal'))
					.hide();

				Swal.fire({
					icon: "success",
					title: "Success",
					text: "User updated successfully",
					timer: 1000,
					showConfirmButton: false,
					didClose: () => {
						showLoader();
						handleUsers(currentPage, false);
					}
				});

			} else {

				if (res.includes("email_exists")) {
					$("#editEmailError")
						.text("Email already exists")
						.show();
				}
			}
		}
	});
}
// =============== FILTER ==================
function filterUser() {

	let hasFilter =
		$("#first-name-search").val().trim() !== "" ||
		$("#last-name-search").val().trim() !== "" ||
		$("#email-search").val().trim() !== "" ||
		$("#contact-search").val().trim() !== "" ||
		($("#gender-search").val() && $("#gender-search").val() !== "all") ||
		$("#address-search").val().trim() !== "";

	if (!hasFilter) {

    if (isFilterActive) {
        isFilterActive = false;
        activeFilters = {};
        lastFilterRequest = "";
        loadUsers(1, pageSize);

        return;
    }

    Swal.fire({
        icon: "warning",
        title: "No Filters",
        text: "Please select at least one filter"
    });

    return;
}
	activeFilters = {
        firstNameType: $("#firstnameType").val(),
        firstName: $("#first-name-search").val(),
        lastNameType: $("#lastnameType").val(),
        lastName: $("#last-name-search").val(),
        emailType: $("#emailType").val(),
        email: $("#email-search").val(),
        contactType: $("#contactType").val(),
        contact: $("#contact-search").val(),
        genderType: $("#genderType").val(),
        gender: $("#gender-search").val(),
        addressType: $("#addressType").val(),
        address: $("#address-search").val()
    };

	isFilterActive = true;
	$("#clearFilterBtn").prop("disabled", false);
	currentPage = 1;
	searchUsers(currentPage, pageSize);
}
// ================= PAGINATION ==================
$(document).on("click", ".page-number", function() {
let clickedPage = $(this).data("page");

	if (clickedPage === currentPage) {
		return;
	}

	currentPage = clickedPage;
	handleUsers(currentPage);
});

$(document).on("click", ".next", function() {
	let totalPages = parseInt($("#totalPages").val());

	if (currentPage < totalPages) {
		currentPage++;
		handleUsers(currentPage);
	}
});

$(document).on("click", ".prev", function() {
	if (currentPage > 1) {
		currentPage--;
		handleUsers(currentPage);
	}
});

$(document).on("change", "#pageSizeSelect", function() {
	pageSize = parseInt($(this).val());
	currentPage = 1;
	handleUsers(currentPage);
});

$(document).on("click", "#goBtn", function() {
	let goPage = parseInt($("#goToPage").val());
	let totalPages = parseInt($("#totalPages").val());


	if (!goPage || goPage < 1 || goPage > totalPages) {
		$("#pageError")
			.text("Invalid page number")
			.show();
		return;
	}
	if(currentPage===goPage){
		return;
	}

	$("#pageError").hide();

	currentPage = goPage;
	handleUsers(currentPage);
	$("#goToPage").val(goPage);
});


function deleteUser() {
	$(document).on("click", "#deleteBtn", function() {

		let selectedUser = $(".userCheckbox:checked").map(function() {
			return $(this).val();//till here it will be jquery object agter get() it'll' be JS array
		}).get();

		if (!selectedUser.length) {
			Swal.fire({
				icon: "warning",
				title: "No Selection",
				text: "Select at least one user to delete"
			});
			return;
		}

		Swal.fire({
			title: "Are you sure?",
			text: "You won't be able to recover these users!",
			icon: "warning",
			showCancelButton: true,
			confirmButtonColor: "#d33",
			cancelButtonColor: "#3085d6",
			confirmButtonText: "Yes, delete it!"
		}).then((result) => {

			if (result.isConfirmed) {

				$.ajax({
					url: "UserController",
					type: "POST",
					data: {
						action: "delete",
						userKeys: selectedUser.join(","),//["1","4","9"]->"1,4,9"(array->string format)
						page: currentPage
					},
					success: function(response) {

						let res = response.trim().toLowerCase();

						if (res === "session_expired") {
							window.location.href = "SessionExpired.jsp";
							return;
						}

						if (response.toLowerCase().includes("success")) {



							let currentRows = $("#userTableWrapper tbody tr")
								.has("input.userCheckbox").length;

							if (selectedUser.length >= currentRows && currentPage > 1) {
								currentPage--;
							}


							Swal.fire({
								icon: "success",
								title: "Deleted!",
								text: "Users deleted successfully",
								timer: 500,
								showConfirmButton: false,
								didClose: () => {

									showLoader();
									handleUsers(currentPage, false);
								}
							});

						} else {
							Swal.fire("Error", response, "error");
						}
					},
					error: function() {
						Swal.fire("Error", "Delete failed", "error");
					}
				});
			}
		});
	});
}


function deleteSingleUser(button) {

	let row = $(button).closest("tr");
	let userKey = row.find(".userCheckbox").val();

	Swal.fire({
		title: "Are you sure?",
		text: "This user will be permanently deleted!",
		icon: "warning",
		showCancelButton: true,
		confirmButtonColor: "#d33",
		cancelButtonColor: "#3085d6",
		confirmButtonText: "Yes, delete it!"
	}).then((result) => {

		if (result.isConfirmed) {

			$.ajax({
				url: "UserController",
				type: "POST",
				data: {
					action: "delete",
					userKeys: userKey,
					page: currentPage
				},
				success: function(response) {

					let res = response.trim().toLowerCase();

					if (res === "session_expired") {
						window.location.href = "SessionExpired.jsp";
						return;
					}

					if (res.includes("success")) {


						setTimeout(() => {

							let currentRows = $("#userTableWrapper tbody tr")
								.has("input.userCheckbox").length;

							if (currentRows === 1 && currentPage > 1) {
								currentPage--;
							}

							handleUsers(currentPage);

						}, 100);



						Swal.fire({
							icon: "success",
							title: "Deleted!",
							text: "User deleted successfully",
							timer: 500,
							showConfirmButton: false,
							didClose: () => {

								showLoader();
								handleUsers(currentPage);
							}
						});

					} else {
						Swal.fire("Error", response, "error");
					}
				},
				error: function() {
					Swal.fire("Error", "Delete failed", "error");
				}
			});
		}
	});
}

// ============ USER LOADING FUNCTIONS ============
function loadUsers(page = 1, pageSize = 10) {

 

    showLoader();

    $.ajax({
        url: "user.jsp",
        type: "GET",
        data: {
            page: page,
            pageSize: pageSize,
            sortColumn: sortColumn,
            sortOrder: sortOrder
        },
        success: function(response) {
            let res = response.trim().toLowerCase();

            if (res === "session_expired") {
                window.location.href = "SessionExpired.jsp";
                return;
            }

            $("#tableContainer").html(response);
            hideLoader(); 
            currentPage = page;
            $("#goToPage").val(currentPage);
            applySortHighlight();
        },
        error: function() {  
            hideLoader();
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Failed to load users'
            });
        }
    });
}

function searchUsers(page = 1, pageSize = 10) {
	  let currentRequest = JSON.stringify({ //JSON.stringify because in js we can't directly compare objects'
        page,
        pageSize,
        sortColumn,
        sortOrder,
        filters: activeFilters
    });

    if (lastFilterRequest === currentRequest) {
        return;
    }

    lastFilterRequest = currentRequest;
    showLoader();

    let f = activeFilters || {}; 

    $.ajax({
        url: "user.jsp",
        type: "POST",
        data: {
            action: "filter",
            page: page,
            pageSize: pageSize,
            sortColumn: sortColumn || '',
            sortOrder: sortOrder || '',
            firstNameType: f.firstNameType,
            firstName: f.firstName,
            lastNameType: f.lastNameType,
            lastName: f.lastName,
            emailType: f.emailType,
            email: f.email,
            contactType: f.contactType,
            contact: f.contact,
            genderType: f.genderType,
            gender: f.gender,
            addressType: f.addressType,
            address: f.address
        },
        success: function(res) {
            let response = res.trim().toLowerCase();
            if (response === "session_expired") {
                window.location.href = "SessionExpired.jsp";
                return;
            }
            $("#tableContainer").html(res);
           
            currentPage = page;
            $("#goToPage").val(currentPage);
            applySortHighlight();
            hideLoader();
        }
    });
}

$(document).ready(function() {
	initPage();
});



function handleUsers(page = 1) {


	currentPage = page;

	if (isFilterActive) {
		searchUsers(currentPage, pageSize);
	} else {
		loadUsers(currentPage, pageSize);
	}
}

function initPage() {
    currentPage = 1;
    activeFilters = {};
    lastFilterRequest = "";

    sortColumn = "";
    sortOrder = "";
    isFilterActive = false;

    $(".sort-icons i").removeClass("active-sort");


    loadUsers(1, pageSize);
}

function clearFilters() {
	let hasAnyValue =
        $("#first-name-search").val().trim() !== "" ||
        $("#last-name-search").val().trim() !== "" ||
        $("#email-search").val().trim() !== "" ||
        $("#contact-search").val().trim() !== "" ||
        $("#address-search").val().trim() !== "" ||
        ($("#gender-search").val() && $("#gender-search").val() !== "all");

    if (!hasAnyValue && !sortColumn && !sortOrder) {
    return;
}
    $("#first-name-search").val("");
    $("#last-name-search").val("");
    $("#email-search").val("");
    $("#contact-search").val("");
    $("#address-search").val("");

    $("select").prop("selectedIndex", 0);

    currentPage = 1;

    activeFilters = {};

    lastFilterRequest = "";

    sortColumn = "";
    sortOrder = "";

    isFilterActive = false;

    $(".sort-icons i").removeClass("active-sort");

   loadUsers(1, 10, true);
}
function editAdminBtn(btn) {

	const isEditing = $(btn).hasClass("activeEdit");

	$(".editField").prop("readonly", isEditing);
	$("#gender").prop("disabled", isEditing);

	$("#profileFooter").toggleClass("d-none");
	$(btn).toggleClass("activeEdit");
}


function logoutBtn(e) {
	e.preventDefault();

	Swal.fire({
		title: "Are you sure?",
		text: "Do you want to logout?",
		icon: "warning",
		showCancelButton: true,
		confirmButtonColor: "#d33",
		cancelButtonColor: "#3085d6",
		confirmButtonText: "Logout",
		cancelButtonText: "Cancel"
	}).then((result) => {
		if (result.isConfirmed) {

			$.ajax({
				url: "loginController",
				type: "post",
				data: { action: "logout" },

				success: function() {

					Swal.fire({
						icon: "success",
						title: "Logged out",
						text: "You have been logged out successfully",
						timer: 1200,
						showConfirmButton: false
					});

					setTimeout(() => {
						window.location.href = "Login.html";
					}, 1200);
				},

				error: function() {
					Swal.fire({
						icon: "error",
						title: "Error",
						text: "Logout failed"
					});
				}
			});
		}
	});
}
function updateAdminProfile(e) {
	e.preventDefault();
	$(".error").hide();

	let firstName = $("#firstname").val().trim();
	let middleName = $("#middlename").val().trim();
	let lastName = $("#lastname").val().trim();
	let email = $("#email").val().trim();
	let address = $("#address").val().trim();
	let gender = $("#gender").val();
	let dob = $("#dob").val();
	let contact = $("#contact").val();
	let adminid = $("#reg_adminid").val(); 
	let isValid=true;
	let today = new Date();
	today.setHours(0, 0, 0, 0);

	let selectedDob = new Date(dob);
	if (!firstName) {
		$("#firstname-error").text("First name cannot be blank").show();
		isValid = false;
	} else if (!/^[A-Za-z]+$/.test(firstName)) {
		$("#firstname-error").text("First name must be alphabets").show();
		isValid = false;
	}

	if (!lastName) {
		$("#lastname-error").text("Last name cannot be blank").show();
		isValid = false;
	} else if (!/^[A-Za-z]+$/.test(lastName)) {
		$("#lastname-error").text("Last name must be alphabets").show();
		isValid = false;
	}

	if (!email) {
		$("#email-error").text("Email cannot be blank").show();
		isValid = false;
	} else if (!/^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/.test(email)) {
		$("#email-error").text("Invalid email format").show();
		isValid = false;
	}

	if (!address) {
		$("#address-error").text("Address cannot be blank").show();
		isValid = false;
	}
	if (!contact) {
		$("#contact-length-error").text("Contact cannot be blank").show();
		isValid = false;
	} else if (contact=== "" || !/^[0-9]{10}$/.test(contact)) {
		$("#contact-length-error").text("Invalid digits").show();
		isValid = false;
	}

	if (!dob) {

		$("#dob-error").text("DOB required").show();
		isValid = false;

	} else if (selectedDob >= today) {

		$("#dob-error")
			.text("Current date and future dates are not allowed")
			.show();

		isValid = false;
	}

	if (!isValid) {
		return;
	}
	$.ajax({
		url: "loginController",
		type: "post",
		data: {
			action: "updateProfile",
			adminid: adminid,
			firstName: firstName,
			middleName: middleName,
			lastName: lastName,
			email: email,
			address: address,
			gender: gender,
			dob: dob,
			contact: contact
		},
		success: function(response) {
			response = response.trim().toLowerCase();

			if (response === "session_expired") {
				window.location.href = "sessionExpired.jsp";
				return;
			}

			if (response.includes("success")) {

				$("#profileModal").modal('hide');

				Swal.fire({
					icon: 'success',
					title: 'Success',
					text: 'Profile updated successfully',
					timer: 1500,
					showConfirmButton: false
				});

				

				$("#firstname").val(firstname);
				$("#middlename").val(middlename);
				$("#lastname").val(lastname);
				$("#email").val(email);
				$("#address").val(address);
				$("#gender").val(gender);
				$("#dob").val(dob);
				$("#contact").val(contact);

				$("#editBtn").removeClass("activeEdit");
				$(".editField").prop("readonly", true);
				$("#gender").prop("disabled", true);
				$("#profileFooter").addClass("d-none");

			} else {
				alert(response);
			}
		},
		error: function(xhr, status, error) {
			alert("Server error: " + error);
		}
	});
}
$('#profileModal').on('hidden.bs.modal', function () {

    loadProfile()
    $(".editField").prop("readonly", true);
	$("#gender").prop("disabled", true);
	$("#profileFooter").addClass("d-none");
	$("#editBtn").removeClass("activeEdit");
	$(".error").hide().text("");

});

function loadProfile() {

	$.ajax({
		url: "loginController",
		type: "post",
		data: { action: "getProfile" },
		success: function(response) {
			let res = response.trim().toLowerCase();

			if (res === "session_expired") {
				window.location.href = "sessionExpired.jsp";
				return;
			}
			let data = response.split("|");

			$("#firstname").val(data[0]);
			$("#middlename").val(data[1]);
			$("#lastname").val(data[2]);
			$("#email").val(data[3]);
			$("#contact").val(data[4]);
			$("#address").val(data[5]);
			$("#gender").val(data[6]);
			$("#dob").val(data[7]);

			$("#navName").text(data[0]);
		}
	});

}

$(document).ready(function() {
	loadProfile();
});

function toggleUserPassword() {
	$(".toggle-password").off("click").on("click", function() {
		let input = $($(this).attr("data-target"));
		let icon = $(this).find("i");

		let type = input.attr("type") === "password" ? "text" : "password";
		input.attr("type", type);

		icon.toggleClass("bi-eye bi-eye-slash");
	});
}

function clearPassword() {
	$("#currentPassword").val("");
	$("#newPassword").val("");
	$("#confirmPassword").val("");

	$("#currentPasswordError").text("").hide();
	$("#newPasswordError").text("").hide();
	$("#confirmPasswordError").text("").hide();

}
/**------this js is for change password in dashboard page--------- **/
function updateUserPassword() {

	const passwordRegex = /^(?=.{8,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$/;
	let current = $("#currentPassword").val().trim();
	let newPass = $("#newPassword").val().trim();
	let confirm = $("#confirmPassword").val().trim();

	let isValid = true;
	if (!current) {
		$("#currentPasswordError").text("Current password required").show();
		isValid = false;
	}
	else if (!passwordRegex.test(current)) {
		$("#currentPasswordError")
			.text("Min 8 chars with uppercase, lowercase, number & special char")
			.show();
		isValid = false;
	}
	else {
		$("#currentPasswordError").hide();
	}
	if (!newPass) {
		$("#newPasswordError").text("New password required").show();
		isValid = false;
	}

	else if (!passwordRegex.test(newPass)) {
		$("#newPasswordError")
			.text("Min 8 chars with uppercase, lowercase, number & special char")
			.show();
		isValid = false;
	}
	else {
		$("#newPasswordError").hide();
	}

	if (!confirm) {
		$("#confirmPasswordError").text("Confirm password required").show();
		isValid = false;
	}
	else if (!passwordRegex.test(confirm)) {
		$("#confirmPasswordError")
			.text("Min 8 chars with uppercase, lowercase, number & special char")
			.show();
		isValid = false;
	}
	else {
		$("#confirmPasswordError").hide();
	}


	if (passwordRegex.test(newPass) && passwordRegex.test(confirm)) {

		if (newPass !== confirm) {

			$("#confirmPasswordError").text("Passwords do not match").show();
			isValid = false;
		} else {
			$("#confirmPasswordError").hide();
		}
	}

	if (!isValid) {
		return;
	};

	let modalEl = document.getElementById('changePasswordModal');
	let modal = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);

	

	$.ajax({
		url: "loginController",
		type: "post",
		data: {
			action: "changePassword",
			currentPassword: current,
			newPassword: newPass,
		},
		success: function(res) {
			res = res.trim();
			let response = res.trim().toLowerCase();
			if (response === "Session_expired") {
				window.location.href = "sessionExpired.jsp";
				return;
			}
			if (response === "current password is incorrect") {

				$("#currentPasswordError")
					.text("Current password is incorrect")
					.show();

				return;
			}

			if (response === "new password cannot be same as old password") {

				$("#newPasswordError")
					.text("New password cannot be same as current password")
					.show();

				return;
			}
           if (response === "password changed successfully") {

	modal.hide();

	$("#currentPassword").val("");
	$("#newPassword").val("");
	$("#confirmPassword").val("");

	setTimeout(() => {

		Swal.fire({
			icon: 'success',
			title: 'Success',
			text: 'Password updated successfully. Please login again.'
		}).then(() => {
			window.location.href = "Login.html";
		});

	}, 100);
}
		}
	});
}
$('#changePasswordModal').on('hidden.bs.modal', function () {

    $("#currentPassword").val("");
    $("#newPassword").val("");
    $("#confirmPassword").val("");

    $(".error").hide().text("");
      $(".password-eye")
        .removeClass("bi-eye-slash-fill")
        .addClass("bi-eye-fill");
});
$("#currentPassword, #newPassword, #confirmPassword").on("copy paste cut", function(e) {
    e.preventDefault();
});



function clearAddUserForm() {


    $("#firstName").val("");
    $("#middleName").val("");
    $("#lastName").val("");
    $("#Email").val("");
    $("#Contact").val("");
    $("#Gender").prop("selectedIndex", 0);
    $("#Dob").val("");
    $("#Address").val("");

    $(".error").hide().text("");
}
$(document).ready(function () {

    const errorMap = {
        firstName: "#first-name-error",
        lastName: "#last-name-error",
        Email: "#add-email-error",
        Contact: "#contact-number-error",
        Gender: "#gender-error",
        Dob: "#add-dob-error",
        Address:"#add-address-error",

        editFirstname: "#editFirstNameError",
        editLastname: "#editLastNameError",
        editEmail: "#editEmailError",
        editContact: "#editContactError",
        editGender: "#editGenderError",
        editDob: "#editDobError",
        editAddress: "#editAddressError",

        firstname: "#firstname-error",
        lastname: "#lastname-error",
        email: "#email-error",
        contact: "#contact-error",
        gender: "#gender-error",
        dob: "#dob-error",
        address: "#address-error",
		
		currentPassword: "#currentPasswordError",
        newPassword: "#newPasswordError",
        confirmPassword: "#confirmPasswordError"
    };

    $("#firstName, #lastName, #Email, #Contact, #Dob, #Address, " +
      "#editFirstname, #editLastname, #editEmail, #editContact, #editDob, #editAddress, " +
      "#firstname, #lastname, #email, #contact, #dob, #address, "+
	  "#currentPassword, #newPassword, #confirmPassword")
    .on("keyup", function () {

        let errorSelector = errorMap[this.id];

        if (errorSelector) {
            $(errorSelector).text("").hide();
        }
    });

    $("#Gender, #editGender, #gender").on("change", function () {

        let errorSelector = errorMap[this.id];

        if (errorSelector) {
            $(errorSelector).text("").hide();
        }
    });

});
function downloadPdf(userEmail) {

    window.location.href =
        "UserController?action=downloadPdf&UserEmail="
        + encodeURIComponent(userEmail);//this is used to avoid break of URL caused from spaces,@  it encodes into %20,%40 because url has its own rule that ?starts parameter,&seperates the parameter,=seperates key and value so to encode special charcters
}

document.addEventListener("DOMContentLoaded", function() {

	function updateTime() {
		const now = new Date();
		let hours = now.getHours();
		let minutes = now.getMinutes();
		let seconds = now.getSeconds();

		hours = hours < 10 ? "0" + hours : hours;
		minutes = minutes < 10 ? "0" + minutes : minutes;
		seconds = seconds < 10 ? "0" + seconds : seconds;

		const timeString = hours + ":" + minutes + ":" + seconds;

		const el = document.getElementById("liveTime");
		if (el) {
			el.innerText = timeString;
		}
	}

	setInterval(updateTime, 1000);
	updateTime();
});

