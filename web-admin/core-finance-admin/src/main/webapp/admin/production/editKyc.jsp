<%@include file="header.jsp" %>

        <!-- page content -->
        <div class="right_col" role="main">
          <div class="">
            <div class="page-title">
              <div class="title_left">
                <h3>Manage Member</h3>
              </div>

              <div class="title_right">
                <div class="col-md-5 col-sm-5 col-xs-12 form-group pull-right top_search">
                 <div class="input-group">
                    <br /> <br /> </div>
                </div>
              </div>
            </div>
            <div class="clearfix"></div>
            <div class="row">
              <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="x_panel">
                  <div class="x_title">
                    <h2>Member Details</h2>
                    <ul class="nav navbar-right panel_toolbox">
                      <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                      </li>
                      <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"><i class="fa fa-wrench"></i></a>
                        <ul class="dropdown-menu" role="menu">
                          <li><a href="#">Settings</a>
                          </li>
                        </ul>
                      </li>
                      <li><a class="close-link"><i class="fa fa-close"></i></a>
                      </li>
                    </ul>
                    <div class="clearfix"></div>
                  </div>
                 
                   <div class="x_content">
        			 <br />
                    <form id="upgradememberform" data-parsley-validate action="/admin/kyc" method="POST" 
                    	modelAttribute="upgrademember" enctype="multipart/form-data" class="form-horizontal form-label-left">
					  		<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="memberDate">
									Created Date
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="createdDate" name="createdDate" value="${createdDate}" disabled
										class="form-control col-md-7 col-xs-12">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="memberGroup">
									Group
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="groupName" name="groupName" value="${groupName}" disabled
										class="form-control col-md-7 col-xs-12">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="memberUsername">
									Username
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="username" name="username" value="${username}" disabled
										required="required" class="form-control col-md-7 col-xs-12">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="memberName">
									Name
									<span class="required">*</span>
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="name" name="name" value="${name}" disabled
										required="required" class="form-control col-md-7 col-xs-12">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="memberEmail">
									Email
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="email" name="email" value="${email}" disabled
										required="required" class="form-control col-md-7 col-xs-12">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="memberMsisdn">
									Mobile No
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="msisdn" name="msisdn" value="${username}" readonly
										required="required" class="form-control col-md-7 col-xs-12">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="memberAddress">
									Address
									<span class="required">*</span>
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="address" name="address" value="${address}" disabled
										required="required" class="form-control col-md-7 col-xs-12">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="memberIDCard">
									ID Card No
									<span class="required">*</span>
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="idCard" name="idCard" value="${idCard}" disabled
										required="required" class="form-control col-md-7 col-xs-12">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="memberMother">
									Mother Maiden Name
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="motherName" name="motherName" value="${motherName}" disabled
										 class="form-control col-md-7 col-xs-12">
								</div>
							</div>
							<div class="form-group">
										<label class="control-label col-md-3 col-sm-3 col-xs-12"
											for="work">
											Work
										</label>
										<div class="col-md-6 col-sm-6 col-xs-12">
											<input type="text" id="work" name="work" value="${work}"
												class="form-control col-md-7 col-xs-12">
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-md-3 col-sm-3 col-xs-12"
											for="sex">
											Sex
										</label>
										<div class="col-md-6 col-sm-6 col-xs-12">
											<input type="text" id="sex" name="sex" value="${sex}"
												class="form-control col-md-7 col-xs-12">
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-md-3 col-sm-3 col-xs-12"
											for="nationality">
											Nationality
										</label>
										<div class="col-md-6 col-sm-6 col-xs-12">
											<input type="text" id="nationality" name="nationality" value="${nationality}"
												class="form-control col-md-7 col-xs-12">
										</div>
									</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="memberPOB">
									Place of Birth
									<span class="required">*</span>
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="pob" name="pob" value="${pob}" disabled
										required="required" class="form-control col-md-7 col-xs-12">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="memberDOB">
									Date of Birth
									<span class="required">*</span>
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
			                    	<input type="text" id="dob" name="dob" value="${dob}" disabled 
			                            	required="required" class="form-control" />
		                       </div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="memberIDCard">
									ID Card
									<span class="required">*</span>
								</label>
								<div class="col-md-3 col-sm-3 col-xs-12">
									<div class="input-group date">
			                            <img id="myImg" alt="ID Card" width="100" height="100" src="${path1}">
			                          	<!-- The Modal -->
										<div id="myModal" class="modal">
										  <span class="close" onclick="spanClick()">&times;</span>
										  <img class="modal-content" id="img01">
										  <div id="caption"></div>
										</div>	
			                        </div>
			                    </div>
		                    </div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="memberPicture&IDCard">
									Picture with ID Card
									<span class="required">*</span>
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<div class="input-group date">
			                            <img id="myImg2" alt="Picture with ID Card" width="100" height="100" src="${path2}">
			                          	<!-- The Modal -->
										<div id="myModal2" class="modal">
										  <span class="close" onclick="spanClick2()">&times;</span>
										  <img class="modal-content" id="img02">
										  <div id="caption2"></div>
										</div>	
			                        </div>
		                       </div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="memberSignature">
									Signature
									<span class="required">*</span>
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<div class="input-group date">
			                            <img id="myImg3" alt="Signature" width="100" height="100" src="${path3}">
			                          	<!-- The Modal -->
										<div id="myModal3" class="modal">
										  <span class="close" onclick="spanClick3()">&times;</span>
										  <img class="modal-content" id="img03">
										  <div id="caption3"></div>
										</div>	
			                        </div>
		                       </div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="memberDescription">
									Description for Confirmation
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
			                    	<input type="text" id="description" name="description" value="${description}" disabled
			                            class="form-control" />
		                       </div>
							</div>
							
							<div class="form-group">
							  <input id="type" name="type" value="kyc" class="date-picker form-control col-md-7 col-xs-12" type="hidden">
	                          <input id="id" name="id" value="${id}" class="date-picker form-control col-md-7 col-xs-12" type="hidden">
	                          <input id="username" name="username" value="${member.username}" class="date-picker form-control col-md-7 col-xs-12" type="hidden">
	                          <!--input id="sessionID" name="sessionID" value="${sessionID}" class="date-picker form-control col-md-7 col-xs-12" type="hidden"-->
	                      	</div>
	                      	
		                  <div class="ln_solid"></div>
	                      <div class="form-group">
	                        <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
	                  		  <button name="editkyc" id="editkyc" class="btn btn-primary" type="button">Edit</button>
	                          <button name="confirm" id="confirmedit" type="submit" value="edit" class="btn btn-success">Submit</button>
	                          <c:choose>
							    <c:when test="${member.groupName == 'ADMIN'}">
							        <button name="confirm" id="confirmedit" type="submit" value="confirmkyc" class="btn btn-success">Confirm</button>
							    </c:when>
							    <c:otherwise>
							        <button name="confirm" id="confirmedit" type="submit" value="confirmkyc" disabled class="btn btn-success">Confirm</button>
							    </c:otherwise>
							</c:choose>
	                        </div>
	                      </div>
                    </form>
                    
               </div>

              </div>
            </div>
          </div>
        </div>
	</div>
        <!-- /page content -->

	<%@include file="footer.jsp" %>
	<!-- Initialize datetimepicker -->
	<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
	<link rel="stylesheet" href="/resources/demos/style.css">
	<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	<script>
	  $( function() {
	    $( "#dob" ).datepicker({
	      yearRange: "-40:+0",
	      changeMonth: true,
	      changeYear: true
	    });
	  } );
	</script>
	
	<script>
			// Get the modal
			var modal = document.getElementById('myModal');
			var modal2 = document.getElementById('myModal2');
			var modal3 = document.getElementById('myModal3');
			
			// Get the image and insert it inside the modal - use its "alt" text as a caption
			var img = document.getElementById('myImg');
			var img2 = document.getElementById('myImg2');
			var img3 = document.getElementById('myImg3');
			
			var modalImg = document.getElementById("img01");
			var modalImg2 = document.getElementById("img02");
			var modalImg3 = document.getElementById("img03");
						
			img.onclick = function(){
			    modal.style.display = "block";
			    modalImg.src = this.src;
			    captionText.innerHTML = this.alt;
			    
			}
			
			img2.onclick = function(){
			    modal2.style.display = "block";
			    modalImg2.src = this.src;
			    captionText.innerHTML = this.alt;
			    
			}
			
			img3.onclick = function(){
			    modal3.style.display = "block";
			    modalImg3.src = this.src;
			    captionText.innerHTML = this.alt;
			    
			}
			
			// Get the <span> element that closes the modal
			var span = document.getElementsByClassName("close")[0];
			
			// When the user clicks on <span> (x), close the modal
			spanClick = function() { 
			    modal.style.display = "none";
			}
			
			spanClick2 = function() { 
			    modal2.style.display = "none";
			}
			
			spanClick3 = function() { 
			    modal3.style.display = "none";
			}
			
		</script>
		
		<script type='text/javascript'>
		   $('#editkyc').click(function() {
	            if ($('#name').attr('disabled')) {
	            	$('#name').removeAttr('disabled');
	                $('#address').removeAttr('disabled');
	                $('#idCard').removeAttr('disabled');
	 				$('#motherName').removeAttr('disabled');
	 				$('#pob').removeAttr('disabled');
	 				$('#dob').removeAttr('disabled');
	            }
	            else {
	                $('#name').attr({
	                    'disabled': 'disabled'
		            });
		                $('#email').attr({
		                    'disabled': 'disabled'
		            });
		            	 $('#bankaccount').attr({
		                    'disabled': 'disabled'
		            });
	        	};
			});
		</script> 
	</body>
</html>