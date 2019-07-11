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
                    <h2>Search Member</h2>
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
                    <form id="upgradememberform1" data-parsley-validate action="/admin/upgradeMember" method="POST" 
                    	modelAttribute="upgrademember" class="form-horizontal form-label-left">
					  		<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="memberDate">
									Created Date
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="createdDate" name="createdDate" value="${createdDate}" readonly
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
									<input type="text" id="username" name="username" value="${username}" readonly
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
									<input type="text" id="name1" name="name1" value="${name}"
										required="required" class="form-control col-md-7 col-xs-12">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="memberEmail">
									Email
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="email" name="email" value="${email}" readonly
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
									<input type="text" id="address1" name="address1" value="${address}"
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
									<input type="text" id="idCard1" name="idCard1" value="${idCard}"
										required="required" class="form-control col-md-7 col-xs-12">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="memberMother">
									Mother Maiden Name
									<span class="required">*</span>
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="motherName1" name="motherName1" value="${motherName}" 
										required="required" class="form-control col-md-7 col-xs-12">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="memberPOB">
									Place of Birth
									<span class="required">*</span>
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="pob1" name="pob1" value="${pob}"
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
									<div class="input-group date">
			                            <input type="text" id="dob1" name="dob1" value="${dob}" required="required" class="form-control" />
			                            <span class="input-group-addon">
			                               <span class="glyphicon glyphicon-calendar"></span>
			                            </span>
			                        </div>
		                       </div>
							</div>
                    </form>
                    
                    <form id="upgradememberform2" data-parsley-validate action="/admin/upgradeMember" method="POST" 
                    	modelAttribute="uploadFiles" enctype="multipart/form-data" class="form-horizontal form-label-left">
                    		<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="memberPicture1">
									ID Card
									<span class="required">*</span>
								</label>
								<div class="form-group col-md-6 col-sm-6 col-xs-12">
									<input type="file" id="files1" name="files[0]" required="required" accept=".jpg, .jpeg, .png" onchange="return fileValidation()">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="memberPicture2">
									Picture with ID Card
									<span class="required">*</span>
								</label>
								<div class="form-group col-md-6 col-sm-6 col-xs-12">
									<input type="file" id="files2" name="files[1]" required="required" accept=".jpg, .jpeg, .png" onchange="return fileValidation2()">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="memberPicture3">
									Signature
									<span class="required">*</span>
								</label>
								<div class="form-group col-md-6 col-sm-6 col-xs-12">
									<input type="file" id="files3" name="files[2]" required="required" accept=".jpg, .jpeg, .png" onchange="return fileValidation3()">
								</div>
							</div>
                      
                       <div class="form-group">
                          <input id="username" name="username" value="${member.username}" class="date-picker form-control col-md-7 col-xs-12" type="hidden">
                          <!--input id="sessionID" name="sessionID" value="${sessionID}" class="date-picker form-control col-md-7 col-xs-12" type="hidden"-->
                      	  <input type="hidden" id="usernameMember" name="usernameMember" value="${username}" class="date-picker form-control col-md-7 col-xs-12">
						  <input type="hidden" id="name" name="name" required="required" class="date-picker form-control col-md-7 col-xs-12">
						  <input type="hidden" id="email" name="email" value="${email}" class="date-picker form-control col-md-7 col-xs-12">
						  <input type="hidden" id="msisdn" name="msisdn" value="${username}" class="date-picker form-control col-md-7 col-xs-12">
						  <input type="hidden" id="address" name="address" class="date-picker form-control col-md-7 col-xs-12">
						  <input type="hidden" id="idCard" name="idCard" class="date-picker form-control col-md-7 col-xs-12">
						  <input type="hidden" id="motherName" name="motherName"  class="date-picker form-control col-md-7 col-xs-12">
						  <input type="hidden" id="pob" name="pob" class="date-picker form-control col-md-7 col-xs-12">
						  <input type="hidden" id="dob" name="dob" class="date-picker form-control col-md-7 col-xs-12">
                      </div>
                
                    </form>
                    <div class="ln_solid"></div>
                      <div class="form-group">
                        <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
                  		  <button class="btn btn-primary" type="button" onclick="history.back()">Back</button>
                          <button type="submit" class="btn btn-success" onclick="submitForms()">Submit</button>
                        </div>
                      </div>
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
		    $( "#dob1" ).datepicker({
		      yearRange: "-40:+0",
		      changeMonth: true,
		      changeYear: true
		    });
		  } );
		</script>
		
		<script>
		    submitForms = function(){
		    	$('#name').val($('#name1').val());
		    	$('#address').val($('#address1').val());
		    	$('#idCard').val($('#idCard1').val());
		    	$('#motherName').val($('#motherName1').val());
		    	$('#pob').val($('#pob1').val());
		    	$('#dob').val($('#dob1').val());
		    	
		        document.getElementById("upgradememberform1").submit();
		        document.getElementById("upgradememberform2").submit();
		    }
		</script>
		
		<script>
		    function fileValidation(){
		    	var fileInput = document.getElementById('files1');
			    var filePath = fileInput.value;
			    var allowedExtensions = /(\.jpg|\.jpeg|\.png)$/i;
			    if(!allowedExtensions.exec(filePath)){
			        alert('Please upload file having extensions .jpeg/.jpg/.png only.');
			        fileInput.value = '';
			        return false;
			    }else{
			    	
			    	return true;
			    }
		    }
		</script>
		
		<script>
		    function fileValidation2(){
		    	var fileInput = document.getElementById('files2');
			    var filePath = fileInput.value;
			    var allowedExtensions = /(\.jpg|\.jpeg|\.png)$/i;
			    if(!allowedExtensions.exec(filePath)){
			        alert('Please upload file having extensions .jpeg/.jpg/.png only.');
			        fileInput.value = '';
			        return false;
			    }else{
			    	
			    	return true;
			    }
		    }
		</script>
		
		<script>
		    function fileValidation3(){
		    	var fileInput = document.getElementById('files3');
			    var filePath = fileInput.value;
			    var allowedExtensions = /(\.jpg|\.jpeg|\.png)$/i;
			    if(!allowedExtensions.exec(filePath)){
			        alert('Please upload file having extensions .jpeg/.jpg/.png only.');
			        fileInput.value = '';
			        return false;
			    }else{
			    	
			    	return true;
			    }
		    }
		</script>
	</body>
</html>