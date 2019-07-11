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
                    <h2>Edit Member</h2>
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
                    <form id="editmemberform" data-parsley-validate action="/admin/editMemberForm" method="POST" 
                    	modelAttribute="upgrademember" class="form-horizontal form-label-left">
					  		<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="memberDate">
									Created Date
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="createdDate" name="createdDate" value="${createdDate}"
										class="form-control col-md-7 col-xs-12">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="memberGroup">
									Group
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="groupName" name="groupName" value="${groupName}" readonly
										class="form-control col-md-7 col-xs-12">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="memberUsername">
									Username
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="username" name="username" value="${username}"
										required="required" class="form-control col-md-7 col-xs-12">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="memberName">
									Name
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="name" name="name" value="${name}" 
										required="required" class="form-control col-md-7 col-xs-12">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="memberEmail">
									Email
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="email" name="email" value="${email}"
										required="required" class="form-control col-md-7 col-xs-12">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="memberMsisdn">
									Mobile No
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="msisdn" name="msisdn" value="${msisdn}"
										required="required" class="form-control col-md-7 col-xs-12">
								</div>
							</div>
							
							<c:choose>
							    <c:when test="${groupName == 'UNREGISTERED'}">
									
								</c:when>
								<c:otherwise>
									<div class="form-group">
										<label class="control-label col-md-3 col-sm-3 col-xs-12"
											for="memberAddress">
											Address
										</label>
										<div class="col-md-6 col-sm-6 col-xs-12">
											<input type="text" id="address" name="address" value="${address}"
												class="form-control col-md-7 col-xs-12">
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-md-3 col-sm-3 col-xs-12"
											for="memberIDCard">
											ID Card No
										</label>
										<div class="col-md-6 col-sm-6 col-xs-12">
											<input type="text" id="idCard" name="idCard" value="${idCard}"
												class="form-control col-md-7 col-xs-12">
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-md-3 col-sm-3 col-xs-12"
											for="memberMother">
											Mother Maiden Name
										</label>
										<div class="col-md-6 col-sm-6 col-xs-12">
											<input type="text" id="motherName" name="motherName" value="${motherName}"
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
										</label>
										<div class="col-md-6 col-sm-6 col-xs-12">
											<input type="text" id="pob" name="pob" value="${pob}"
												class="form-control col-md-7 col-xs-12">
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-md-3 col-sm-3 col-xs-12"
											for="memberDOB">
											Date of Birth
										</label>
										<div class="col-md-6 col-sm-6 col-xs-12">
											<div class="input-group date">
					                            <input type="text" id="dob" name="dob" value="${dob}" class="form-control" />
					                            <span class="input-group-addon">
					                               <span class="glyphicon glyphicon-calendar"></span>
					                            </span>
					                        </div>
				                       </div>
									</div>
									<div class="form-group">
										<label class="control-label col-md-3 col-sm-3 col-xs-12"
											for="memberIDCard">
											ID Card
										</label>
										<div class="col-md-3 col-sm-3 col-xs-12">
											<div>
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
										</label>
										<div class="col-md-6 col-sm-6 col-xs-12">
											<div>
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
										</label>
										<div class="col-md-6 col-sm-6 col-xs-12">
											<div>
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
								</c:otherwise>
							</c:choose>
							<div class="form-group">
							  <input id="type" name="type" value="member" class="date-picker form-control col-md-7 col-xs-12" type="hidden">
	                          <input id="id" name="id" value="${id}" class="date-picker form-control col-md-7 col-xs-12" type="hidden">
	                      	</div>
	                      	
		                  <div class="ln_solid"></div>
	                      <div class="form-group">
	                        <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
	                  		  <button class="btn btn-primary" type="button" onclick="history.back()">Back</button>
	                          <button name="confirm" id="confirmedit" type="submit" value="edit" class="btn btn-success">Submit</button>
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
		
	</body>
</html>