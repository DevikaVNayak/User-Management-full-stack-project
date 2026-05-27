let isVerified = false;
// Login.js

function clearRegisterForm() {
	$(".form2")[0].reset();
	$(".error").hide().text("");
	$(".toggle-icon")
		.removeClass("bi-eye-slash-fill")
		.addClass("bi-eye-fill");
}

function registerAdmin() {

	$(".error").hide();

	let adminId = $("#reg_adminid").val().trim();
	let firstName = $("#firstname").val().trim();
	let middleName = $("#middlename").val().trim();
	let lastName = $("#lastname").val().trim();
	let email = $("#email").val().trim();
	let address = $("#address").val().trim();
	let gender = $("#gender").val().trim();
	let dob = $("#dob").val().trim();
	let newPassword = $("#newpassword").val();
	let confirmPassword = $("#confirmpassword").val();
	let question = $("#QuestionClass").val().trim();
	let answer = $("#answer").val().trim().toLowerCase();
	let rawNumber = $("#contact").val().trim();
	let today = new Date();
	today.setHours(0, 0, 0, 0);
	let selectedDob = new Date(dob);

	let isValid = true;

	if (adminId === "") {
		$("#adminid-error").text("Id cannot be blank").show();
		isValid = false;
	}
	else if (/\s/.test(adminId)) {

		$("#adminid-error").text("Spaces are not allowed").show();
		isValid = false;
	}
	else if (!/^[a-zA-Z0-9]{12}$/.test(adminId)) {
		$("#adminid-error").text("Id should contain 12 letters and numbers").show();
		isValid = false;
	}

	if (firstName === "") {
		$("#firstname-error").text("First name cannot be blank").show();
		isValid = false;
	}
	else if (/\s/.test(firstName)) {

		$("#firstname-error").text("Spaces are not allowed").show();
		isValid = false;
	} else if (!/^[A-Za-z]+$/.test(firstName)) {
		$("#firstname-error").text("must be alphabets").show();

		isValid = false;
	}

	if (middleName !== "" && !/^[A-Za-z]+$/.test(middleName)) {
		$("#middlename-error").text("must be alphabets").show();

		isValid = false;
	}

	if (lastName === "") {
		$("#lastname-error").text("Last name cannot be blank").show();
		isValid = false;
	}
	else if (/\s/.test(lastName)) {
		$("#lastName-error").text("Spaces are not allowed").show();
		isValid = false;
	}
	else if (!/^[A-Za-z]+$/.test(lastName)) {
		$("#lastname-error").text("must be alphabets").show();
		isValid = false;
	}

	if (email === "") {
		$("#email-error").text("Email cannot be blank").show();

		isValid = false;
	} else if (!/^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/.test(email)) {
		$("#email-error").text("invalid email").show();

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

	if (address === "") {
		$("#address-error").text("Enter address").show();
		isValid = false;
	}

	if (rawNumber === "") {
		$("#contact-error").text("Contact cannot be blank").show();
		isValid = false;
	} else if (!/^[0-9]{10}$/.test(rawNumber)) {
		$("#contact-error").text("Enter valid 10-digit number").show();
		isValid = false;
	}

	if (newPassword === "") {
		$("#newpassword-error")
			.text(" cannot be blank")
			.show();
		isValid = false;
	}
	else if (newPassword.includes(" ")) {

		$("#newpassword-error")
			.text("Spaces are not allowed")
			.show();

		isValid = false;
	}
	else if (!/(?=^.{8,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$/.test(newPassword)) {
		$("#newpassword-error").text("Invalid password").show();
		isValid = false;
	}
	if (confirmPassword === "") {
		$("#confirmpassword-error")
			.text("cannot be blank")
			.show();
		isValid = false;
	} else if (confirmPassword.includes(" ")) {

		$("#confirmpassword-error")
			.text("Spaces are not allowed")
			.show();

		isValid = false;
	}
	else if (!/(?=^.{8,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$/.test(confirmPassword)) {
		$("#confirmpassword-error").text("Invalid password").show();
		isValid = false;
	}

	if (newPassword !== confirmPassword) {
		$("#confirmpassword-error").show();
		isValid = false;
	}

	if (gender === "") {
		$("#gender-error").text("It cannot be blank").show();
		isValid = false;
	}

	if (question === "") {
		$("#question-error").show();
		isValid = false;
	}

	if (answer === "") {
		$("#answer-error").text('It cannnot be blank').show();
		isValid = false;
	}
	if (!isValid) {
		return;
	}
	$.ajax({
		url: "loginController",
		type: "post",
		data: {
			action: "register",
			adminId: adminId,
			firstName: firstName,
			middleName: middleName,
			lastName: lastName,
			email: email,
			address: address,
			gender: gender,
			password: newPassword,
			contact: rawNumber,
			dob: dob,
			question: question,
			answer: answer,
		},
		success: function(response) {
			response = response.trim();


			if (response.includes("admin id already exists")) $("#adminid-error").text("Admin id already exists").show();
			if (response.includes("email already exists")) $("#email-error").text("Email already exists").show();
			if (response.includes("phone number already exists")) $("#contact-error").text("Phone number already exists").show();
			if (response.includes("Admin must be at least 18 years old")) $("#dob-error").text("Admin age must be at least 18 years").show();

			if (response.includes("Registered successfully")) {
				$("#register-modal").modal('hide');

				setTimeout(() => {
					showAlert("Registered successfully");
				}, 300);

				$(".form2")[0].reset();

			}
			else {
				showAlert("Registration failed", "error");
			}
			/*else if (response === "empty fields") { //remove
	$("#adminid-error").text("Please fill all required fields").show();
}*/
		},
		error: function() {
			alert("Server error");
		}
	});

}
$(document).ready(function() {

	$("#newpassword, #confirmpassword").on(
		"copy paste cut drag drop",
		function(e) {
			e.preventDefault();
		}
	);

});

$("#loginBtn").click(function() {
	loginAdmin();
});

function loginAdmin() {

	$(".error").hide();

	let adminId = $("#adminid").val().trim();
	let password = $("#adminpassword").val().trim();

	let hasError = false;

	if (adminId === "") {
		$("#adminid-login-error")
			.text("Admin ID required")
			.show();

		hasError = true;
	}

	if (password === "") {
		$("#adminpassword-login-error").text("Admin Password required").show();
		hasError = true;
	}
	else if (password.includes(" ")) {

		$("#adminpassword-login-error")
			.text("Spaces are not allowed")
			.show();

		hasError = true;
	}
	if (hasError) {
		return;
	}

	$.ajax({
		url: "loginController",
		type: "post",
		data: {
			action: "login",
			adminId: adminId,
			adminPassword: password
		},
		success: function(response) {

			if (response === "success") {
				$("#adminid").val("");
				$("#adminpassword").val("");
				window.location.href = window.location.origin + "/DemoProject/dashboard.jsp";
			}
			else if (response === "invalid password") {
				$("#adminpassword-login-error").text("Invalid Password").show();
			}
			else if (response === "admin not found") {
				$("#adminid-login-error").text("Admin ID not found").show();
			}

		},
		error: function() {
			alert("Server error");
		}
	});
}
$(document).on(
	"copy paste cut drag drop",
	"#adminpassword",
	function(e) {
		e.preventDefault();
	}
);
$(document).on(
	"copy paste cut drag drop",
	"#forgotPasswordNewPassword, #forgotPasswordConfirmPassword",
	function(e) {
		e.preventDefault();
	}
);
function toggleEditMode() {
	const btn = document.getElementById('editBtn');
	btn.classList.toggle('active-edit');
}

function toggleLoginPassword() {
	const input = document.getElementById('adminpassword');
	const icon = input.nextElementSibling;

	if (input.type === 'password') {
		input.type = 'text';
		icon.classList.remove('bi-eye');
		icon.classList.add('bi-eye-slash');
	} else {
		input.type = 'password';
		icon.classList.remove('bi-eye-slash');
		icon.classList.add('bi-eye');
	}
}

function togglePassword(inputId, icon) {

	const input = document.getElementById(inputId);

	if (input.type === "password") {

		input.type = "text";

		icon.classList.remove("bi-eye-fill");
		icon.classList.add("bi-eye-slash-fill");

	} else {

		input.type = "password";

		icon.classList.remove("bi-eye-slash-fill");
		icon.classList.add("bi-eye-fill");
	}
}
function verifySecurity() {
	$(".error").hide().text("");
	let adminId = $("#forgotPasswordAdminId").val().trim();
	let question = $("#forgotPasswordQuestion").val().trim();
	let answer = $("#forgotPasswordAnswer").val().trim().toLowerCase();

	let isValid = true;

	if (!adminId) {
		$("#forgot-adminid-error")
			.text("Admin ID required")
			.show();
		isValid = false;
	} else if (!/^[a-zA-Z0-9]{12}$/.test(adminId)) {

		$("#forgot-adminid-error")
			.text("ID should contain 12 letters and numbers")
			.show();

		isValid = false;
	}

	if (!question) {
		$("#forgot-question-error")
			.text("Select question")
			.show();
		isValid = false;
	}

	if (!answer) {
		$("#forgot-answer-error")
			.text("Answer required")
			.show();
		isValid = false;
	}
	if (!isValid) {
		return;
	}

	$.ajax({
		url: "loginController",
		type: "POST",
		data: {
			action: "verifySecurity",
			adminId: adminId,
			question: question,
			answer: answer
		},
		success: function(response) {

			if (response === "success") {
				  isVerified = true;
				$("#forgotPasswordQuestion").val("");
				$("#forgotPasswordAnswer").val("");
				$("#forgotPasswordNewPassword, #forgotPasswordConfirmPassword").prop("disabled", false);

				$("#forgot-modal").modal("hide");
				$("#reset-modal").modal("show");

				alert("Verified successfully");

			} else if (response === "invalid_adminid") {

				$("#forgot-adminid-error")
					.text("Invalid Admin ID")
					.show();
			}

			else if (response === "invalid_question") {

				$("#forgot-question-error")
					.text("Security question does not match")
					.show();
			}

			else if (response === "incorrect_answer") {

				$("#forgot-answer-error")
					.text("Incorrect answer")
					.show();
			}
		}
	});
}
$(document).ready(function() {

    $("#forgot-modal").on("hidden.bs.modal", function() {

        if (!isVerified) {

            $("#forgotPasswordAdminId").val("");
            $("#forgotPasswordQuestion").prop("selectedIndex", 0);
            $("#forgotPasswordAnswer").val("");

            $("#forgot-adminid-error").hide().text("");
            $("#forgot-question-error").hide().text("");
            $("#forgot-answer-error").hide().text("");
        }

    });

});
function resetForgotPassword() {
	$(".error").hide().text("");
	let adminId = $("#forgotPasswordAdminId").val().trim();
	let newPass = $("#forgotPasswordNewPassword").val().trim();
	let confirmPass = $("#forgotPasswordConfirmPassword").val().trim();

	let isValid = true;

	const passwordRegex =
		/^(?=.{8,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$/;

	if (!newPass) {

		$("#forgot-newpassword-error")
			.text("New password required")
			.show();

		isValid = false;

	} else if (!passwordRegex.test(newPass)) {

		$("#forgot-newpassword-error")
			.text("Min 8 chars with uppercase, lowercase, number & special char")
			.show();

		isValid = false;
	}

	if (!confirmPass) {

		$("#forgot-confirmpassword-error")
			.text("Confirm password required")
			.show();

		isValid = false;

	} else if (!passwordRegex.test(confirmPass)) {

		$("#forgot-confirmpassword-error")
			.text("Min 8 chars with uppercase, lowercase, number & special char")
			.show();

		isValid = false;
	}
	else if (newPass !== confirmPass) {

		$("#forgot-confirmpassword-error")
			.text("Passwords do not match")
			.show();

		isValid = false;
	}

	if (!isValid) {
		return;
	}

	$.ajax({
		url: "loginController",
		type: "POST",
		data: {
			action: "resetForgotPassword",
			adminId: adminId,
			password: newPass
		},
		success: function(response) {
			 isVerified = false;
			if (response === "success") {

				alert("Password updated successfully");

				$("#reset-modal").modal("hide");

				$("#forgotPasswordAdminId").val("");

				$("#forgotPasswordAnswer").val("");

				$("#forgotPasswordNewPassword, #forgotPasswordConfirmPassword")
					.val("");

			} else if (response === "same_password") {

				$("#forgot-newpassword-error")
					.text("New password cannot be same as old password")
					.show();

			} else if (response === "unauthorized") {
				alert("Session expired. Please verify again.");

			} else {
				alert("Password update failed");
			}
		}
	});
}
$(document).on("hidden.bs.modal", "#reset-modal", function() {
	clearResetModal();
});

function clearResetModal() {

	$("#forgotPasswordNewPassword, #forgotPasswordConfirmPassword").val("");

	$(".error").hide().text("");

	$(".toggle-password").attr("class", "bi bi-eye-fill toggle-password");
}
$(document).ready(function() {

	const errorMap = {
		reg_adminid: "#adminid-error",
		firstname: "#firstname-error",
		middlename: "#middlename-error",
		lastname: "#lastname-error",
		email: "#email-error",
		address: "#address-error",
		contact: "#contact-error",
		dob: "#dob-error",
		newpassword: "#newpassword-error",
		confirmpassword: "#confirmpassword-error",
		answer: "#answer-error",

		adminid: "#adminid-login-error",
		adminpassword: "#adminpassword-login-error"
	};

	$("#reg_adminid, #firstname, #middlename,#lastname, #email, #address, #contact, " +
		"#dob, #newpassword, #confirmpassword, #answer, " +
		"#adminid, #adminpassword")
		.on("keyup", function() {

			let errorSelector = errorMap[this.id];

			if (errorSelector) {
				$(errorSelector).text("").hide();
			}
		});

	$("#gender, #QuestionClass").on("change", function() {

		$("#gender-error, #question-error").text("").hide();
	});

});


$(document).ready(function() {

	let today = new Date().toISOString().split("T")[0];


	$("#dob").attr("max", today);

});
function showAlert(message) { //this is for custom div
	$("#customAlertMessage").text(message);
	$("#customAlertOverlay").fadeIn();

	setTimeout(() => {
		$("#customAlertOverlay").fadeOut();
	}, 2000);
}


