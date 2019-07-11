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
              <div class="col-md-12 col-sm-12 col-xs-24">
                <div class="x_panel">
                  <div class="x_title">
                    <h2>Member Details</small></h2>
                    <ul class="nav navbar-right panel_toolbox">
                      <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                      </li>
                      <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"><i class="fa fa-wrench"></i></a>
                        <ul class="dropdown-menu" role="menu">
                          <li><a href="#">Settings 1</a>
                          </li>
                        </ul>
                      </li>
                      <li><a class="close-link"><i class="fa fa-close"></i></a>
                      </li>
                    </ul>
                    <div class="clearfix"></div>
                  </div>
                  
                  <div class="x_content">
                    <table id="trxdetailMember" class="table table-striped">
                      <tbody>
                      	<tr>
                          <td width="150">ID</td>
                          <td width="150"> : </td>
                          <td align="right">${id}</td>
                        </tr>
                      	<tr>
                          <td width="150">Status Approval</td>
                          <td width="150"> : </td>
                          <td align="right">${statusApproval}</td>
                        </tr>
                        <tr>
                          <td width="150">Requested Date</td>
                          <td width="150"> : </td>
                          <td align="right">${requestedDate}</td>
                        </tr>
                        <tr>
                          <td width="150">Group</td>
                          <td width="150"> : </td>
                          <td align="right">${groupName}</td>
                        </tr>
                        <tr>
                          <td width="150">Username</td>
                          <td width="150"> : </td>
                          <td align="right">${username}</td>
                        </tr>
                        <tr>
                          <td width="150">Name</td>
                          <td width="150"> : </td>
                          <td align="right">${name}</td>
                        </tr>
                        <tr>
                          <td width="150">Mobile No</td>
                          <td width="150"> : </td>
                          <td align="right">${msisdn}</td>
                        </tr>
                        <tr>
                          <td width="150">Email</td>
                          <td width="150"> : </td>
                          <td align="right">${email}</td>
                        </tr>
                        <tr>
                          <td width="150">Created Date</td>
                          <td width="150"> : </td>
                          <td align="right">${createdDate}</td>
                        </tr>
                        <tr>
                          <td width="150">Address</td>
                          <td width="150"> : </td>
                          <td align="right">${address}</td>
                        </tr>
                        <tr>
                          <td width="150">ID Card Nomor</td>
                          <td width="150"> : </td>
                          <td align="right">${idCard}</td>
                        </tr>
                        <tr>
                          <td width="150">Mother Maiden Name</td>
                          <td width="150"> : </td>
                          <td align="right">${motherName}</td>
                        </tr>
                        <tr>
                          <td width="150">Place of Birth</td>
                          <td width="150"> : </td>
                          <td align="right">${pob}</td>
                        </tr>
                        <tr>
                          <td width="150">Date of Birth</td>
                          <td width="150"> : </td>
                          <td align="right">${dob}</td>
                        </tr>
                        <tr>
                          <td width="150">ID Card</td>
                          <td width="150"> : </td>
                          <td align="right">
                          	<img id="myImg" alt="ID Card" width="100" height="100" src="${path1}">
                          	<!-- The Modal -->
							<div id="myModal" class="modal">
							  <span class="close" onclick="spanClick()">&times;</span>
							  <img class="modal-content" id="img01">
							  <div id="caption"></div>
							</div>	
                          </td>
                        </tr>
                        <tr>
                          <td width="150">Picture with ID Card</td>
                          <td width="150"> : </td>
                          <td align="right">
                          	<img id="myImg2" alt="Picture with ID Card" width="100" height="100" src="${path2}">
                          	<!-- The Modal -->
							<div id="myModal2" class="modal">
							  <span class="close" onclick="spanClick2()">&times;</span>
							  <img class="modal-content" id="img02">
							  <div id="caption2"></div>
							</div>
                          </td>
                        </tr>
                        <tr>
                          <td width="150">Signature</td>
                          <td width="150"> : </td>
                          <td align="right">
                          	<img id="myImg3" alt="Signature" width="100" height="100" src="${path3}">
                          	<!-- The Modal -->
							<div id="myModal3" class="modal">
							  <span class="close" onclick="spanClick3()">&times;</span>
							  <img class="modal-content" id="img03">
							  <div id="caption3"></div>
							</div>
                          </td>
                        </tr>
                        <tr>
                          <td width="150">Description for Confirmation</td>
                          <td width="150"> : </td>
                          <td align="right">
                          	<input type="text" id="description1" name="description1" required="required" class="form-control col-md-7 col-xs-12" style=”text-align:right”>
                          </td>
                        </tr>
                      </tbody>
                    </table>
                    <hr/>
                    					
                    <form id="confirmkycform" data-parsley-validate action="/admin/kyc" method="POST" modelAttribute="confirmkyc" class="form-horizontal form-label-left">
                          <input id="id" name="id" value="${id}" class="date-picker form-control col-md-7 col-xs-12" type="hidden">
          			  	  <input id="username" name="username" value="${member.username}" class="date-picker form-control col-md-7 col-xs-12" type="hidden">
                          <!--input id="sessionID" name="SessionID" value="${sessionID}" class="date-picker form-control col-md-7 col-xs-12" type="hidden"-->
                          <input id="description" name="description" class="date-picker form-control col-md-7 col-xs-12" type="hidden">
                      
                        <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
           				  <button class="btn btn-primary" type="button" onclick="history.back()">Back</button>
                          <button name="confirm" id"reject" type="submit" value="reject" onclick="rejectForm();" class="btn btn-success">Reject</button>
	                      <button name="confirm" id="approve" type="submit" value="approve" class="btn btn-success">Approve</button>
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

	<script>
		function reloadTable(){
			var table = $('#trxtable').DataTable();
			table.ajax.reload( null, false );
		}
	</script>
	
	<script>
		rejectForm = function(){
	    	$('#description').val($('#description1').val());
		};
	</script>
	
		<script>
	 		$("#trxdetailMember")
					.DataTable(
						{
						 "processing" : true,
	       				 "serverSide" : true,
	       				 "dom" : 'Blfrtip',
	     			     "buttons" : [
	                      'csvHtml5'
	     		          ],
	       				 "ajax" : {
	       					 "url" : "/admin/detailMemberData",
	       					 "data" : function ( d ) {
	 		               	  	
	 			           		}
	 			          }
						});
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
