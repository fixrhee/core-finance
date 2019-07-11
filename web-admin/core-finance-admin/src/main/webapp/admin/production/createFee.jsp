<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="header.jsp" %>

        <!-- page content -->
        <div class="right_col" role="main">
          <div class="">
            <div class="page-title">
              <div class="title_left">
                <h3>Transfer Type</h3>
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
                    <h2>Create Fee</h2>
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
                    <form id="feeForm" data-parsley-validate action="/admin/createFeeForm" method="POST" modelAttribute="createFeeModel" class="form-horizontal form-label-left">
					  
					   <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="name">Fee Name <span class="required">*</span> :
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="name" name="name" required="required" class="form-control col-md-7 col-xs-12">
                        </div>
                       </div>
                      
                       <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="description">Description <span class="required">*</span> :
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="description" name="description" required="required" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
              
                       <div class="form-group">
		                <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">Deduct From :</label>
		                <div class="col-md-6 col-sm-6 col-xs-12">
		                  <select name="fromMemberOpt" onchange='changeFuncFromMember(value);' class="select2_single form-control" tabindex="-1">
		                  	  <option name="fromMemberOpt" id="fromMemberOpt" value="0">Source Member</option>
		                  	  <option name="fromMemberOpt" id="fromMemberOpt" value="-1">Destination Member</option>
		                  	  <option name="fromMemberOpt" id="fromMemberOpt" value="-2">Source Parent Member</option>
		                  	  <option name="fromMemberOpt" id="fromMemberOpt" value="-3">Destination Parent Member</option>
		                  	  <option name="fromMemberOpt" id="fromMemberOpt" value="-4">[ Input Username ]</option>
		                    </select>
		                </div>
		              </div>
		              
                       <div id="fromMemberDiv" class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12"><font color="blue">Member Username </span> : </font>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="fromMember" name="fromMember" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
           			  
           			   <div class="form-group">
		                <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">Deduct From Account :</label>
		                <div class="col-md-6 col-sm-6 col-xs-12">
		                  <select name="fromAccountOpt" onchange='changeFuncFromAccount(value);' class="select2_single form-control" tabindex="-1">
		                       	  <option name="fromAccountName" id="fromAccountName" value="0">Source Account</option>
		                  		  <option name="fromAccountName" id="fromAccountName" value="-2">Source Parent Account</option>
		                  		  <option name="fromAccountName" id="fromAccountName" value="-1">Destination Account</option>
		                  	 	  <option name="fromAccountName" id="fromAccountName" value="-3">Destination Parent Account</option>
		                  	 	  <option name="fromAccountName" id="fromAccountName" value="-4">[ Select Account ]</option>
		                    </select>
		                </div>
		              </div>
		              
           			  <div id="fromAccountDiv" class="form-group">
		                <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name"><font color="blue">From Account :</font></label>
		                <div class="col-md-6 col-sm-6 col-xs-12">
		                  <select name="fromAccountName" class="select2_single form-control" tabindex="-1">
		                  	            <c:forEach var="listAccount" items="${listAccount}">
									<option name="fromAccountName" id="fromAccountName" value="${listAccount}">${listAccount}</option>
								</c:forEach>
		                   </select>
		                </div>
		              </div>
		              
		              <div class="form-group">
		                <label class="control-label col-md-3 col-sm-3 col-xs-12" for="toMemberOpt">Credit To :</label>
		                <div class="col-md-6 col-sm-6 col-xs-12">
		                  <select name="toMemberOpt" onchange='changeFuncToMember(value);' class="select2_single form-control" tabindex="-1">
		                  	  <option name="toMemberOpt" id="toMemberOpt" value="-1">Destination Member</option>
		                  	  <option name="toMemberOpt" id="toMemberOpt" value="-3">Destination Parent Member</option>
		                  	  <option name="toMemberOpt" id="toMemberOpt" value="0">Source Member</option>
		                  	  <option name="toMemberOpt" id="toMemberOpt" value="-2">Source Parent Member</option>
		                  	  <option name="toMemberOpt" id="toMemberOpt" value="-4">[ Input Username ]</option>
		                    </select>
		                </div>
		              </div>
		              
                       <div id="toMemberDiv" class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12"><font color="blue">Member Username : </font>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="toMember" name="toMember" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                      
                     <div class="form-group">
		                <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">Credit to Account :</label>
		                <div class="col-md-6 col-sm-6 col-xs-12">
		                  <select name="toAccountOpt" onchange='changeFuncToAccount(value);' class="select2_single form-control" tabindex="-1">
		                  		  <option name="toAccountOpt" id="toAccountOpt" value="-1">Destination Account</option>
		                  	 	  <option name="toAccountOpt" id="toAccountOpt" value="-3">Destination Parent Account</option>
		                      	  <option name="toAccountOpt" id="toAccountOpt" value="0">Source Account</option>
		                  		  <option name="toAccountOpt" id="toAccountOpt" value="-2">Source Parent Account</option>
		                   	 	  <option name="toAccountOpt" id="toAccountOpt" value="-4">[ Select Account ]</option>
		                    </select>
		                </div>
		              </div>
		     
           			  <div id="toAccountDiv" class="form-group">
		                <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name"><font color="blue">To Account :</font></label>
		                <div class="col-md-6 col-sm-6 col-xs-12">
		                  <select name="toAccountName" class="select2_single form-control" tabindex="-1">
		                  	            <c:forEach var="listAccount" items="${listAccount}">
									<option name="toAccountName" id="toAccountName" value="${listAccount}">${listAccount}</option>
								</c:forEach>
		                   </select>
		                </div>
		              </div>
		             
			           <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="minAmount">Minimal Amount <span class="required">*</span> :
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="minAmount" name="minAmount" value="1" required="required" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                      
                       <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="maxAmount">Maximum Amount <span class="required">*</span> :
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="maxAmount" name="maxAmount" value="0" required="required" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                      
		              <hr />
		              
		               <div class="form-group">
                          <label class="control-label col-md-3 col-sm-3 col-xs-12">Filter by Source Member </label>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                           <div class="">
                       		 <label>
                              <input type="checkbox" id="allowFromAllMember" name="allowFromAllMember" class="js-switch" value="1" /> True
                            </label>
                            </div>
                    		 </div>
                    	  </div>
                    	  
                    	  <div class="form-group">
                          <label class="control-label col-md-3 col-sm-3 col-xs-12">Filter by Destination Member </label>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                           <div class="">
                       		 <label>
                              <input type="checkbox" id="allowToAllMember" name="allowToAllMember" class="js-switch" value="1" /> True
                            </label>
                            </div>
                    		 </div>
                    	  </div>
                  
                     <hr />
		
		                <div class="form-group">
                          <label class="control-label col-md-3 col-sm-3 col-xs-12">Enable Fee Periode </label>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                           <div class="">
                       		 <label>
                              <input type="checkbox" id="periode" name="periode" class="js-switch" value="1" onchange='handleChange(this);' /> True
                            </label>
                            </div>
                    		 </div>
                    	  </div>
                    	  
		               <div id="startDateDiv" class="form-group">
                       <label class="control-label col-md-3 col-sm-3 col-xs-12" for="expired">Start Date <span class="required">*</span>
                        </label>
                         <div class="col-md-6 col-sm-6 col-xs-12">
                          <div class='input-group date' id='startDatepicker'>
                            <input type='text' id="startDate" name="startDate" required="required" class="form-control" readonly="readonly" />
                            <span class="input-group-addon">
                               <span class="glyphicon glyphicon-calendar"></span>
                            </span>
                        </div>
                       </div>
                    	</div>
                    	
                    	<div id="endDateDiv" class="form-group">
                       <label class="control-label col-md-3 col-sm-3 col-xs-12" for="expired">End Date <span class="required">*</span>
                        </label>
                         <div class="col-md-6 col-sm-6 col-xs-12">
                          <div class='input-group date' id='endDatepicker'>
                            <input type='text' id="endDate" name="endDate" required="required" class="form-control" readonly="readonly" />
                            <span class="input-group-addon">
                               <span class="glyphicon glyphicon-calendar"></span>
                            </span>
                        </div>
                       </div>
                    	</div>
                    
		              <hr />
		              
		                  <div class="form-group">
                          <label class="control-label col-md-3 col-sm-3 col-xs-12">Deduct Amount </label>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                           <div class="">
                       		 <label>
                              <input type="checkbox" id="deductAmount" name="deductAmount" class="js-switch" value="1"  /> True
                            </label>
                            </div>
                    		 </div>
                    	  </div>
                      
                     <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="fixedAmount">Fixed Amount <span class="required">*</span> :
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="fixedAmount" name="fixedAmount" required="required" value="0" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                      
                       <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="percentage">Percentage <span class="required">*</span> :
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="percentage" name="percentage" required="required" value="0" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                      
                      <hr />
                      
                       <div class="form-group">
                          <label class="control-label col-md-3 col-sm-3 col-xs-12">Priority </label>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                           <div class="">
                       		 <label>
                              <input type="checkbox" id="priority" name="priority" class="js-switch" value="1"  /> True
                            </label>
                            </div>
                    		 </div>
                    	  </div>
                    	  
                    	   <div class="form-group">
                          <label class="control-label col-md-3 col-sm-3 col-xs-12"><font color="red">Enabled </font></label>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                           <div class="">
                       		 <label>
                              <input type="checkbox" id="enabled" name="enabled" class="js-switch" value="1"  /> True
                            </label>
                            </div>
                    		 </div>
                    	  </div>
            
                      
                      <div class="form-group">
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="hidden" id="transferTypeID" name="transferTypeID" required="required" value="${transferTypeID}" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                      
                      
                      <div class="ln_solid"></div>
                      <div class="form-group">
                        <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
                  		  <button class="btn btn-primary" type="reset">Reset</button>
                          <button type="submit" class="btn btn-success">Submit</button>
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
<script>
    $('#startDatepicker').datetimepicker({
   		minDate: moment().add('days', 1),
   		maxDate: moment().add('days', 90),
        ignoreReadonly: true,
        allowInputToggle: true
    });
</script>
<script>
    $('#endDatepicker').datetimepicker({
   		minDate: moment().add('days', 2),
   		maxDate: moment().add('days', 90),
        ignoreReadonly: true,
        allowInputToggle: true
    });
</script>

<script>
   document.getElementById("startDateDiv").style.display = "none";
   document.getElementById("endDateDiv").style.display = "none";
   document.getElementById("fromMemberDiv").style.display = "none";
   document.getElementById("toMemberDiv").style.display = "none";
   document.getElementById("fromAccountDiv").style.display = "none";
   document.getElementById("toAccountDiv").style.display = "none";
</script>

<script>
function handleChange(checkbox) {
    if(checkbox.checked == true){
    document.getElementById("startDateDiv").style.display = "block"
    document.getElementById("endDateDiv").style.display = "block"
    }else{
    document.getElementById("startDateDiv").style.display = "none"
    document.getElementById("endDateDiv").style.display = "none"
   }
}
</script>

<script>
 function changeFuncFromMember(value) {
 	if(value == '-4'){
	   	 document.getElementById("fromMemberDiv").style.display = "block"
    }else{
  		document.getElementById("fromMemberDiv").style.display = "none";
   	}
   }
</script>

<script>
 function changeFuncToMember(value) {
 	if(value == '-4'){
    		document.getElementById("toMemberDiv").style.display = "block"
    }else{
   	 document.getElementById("toMemberDiv").style.display = "none";
   	}
   }
</script>

<script>
 function changeFuncFromAccount(value) {
 	if(value == '-4'){
    		document.getElementById("fromAccountDiv").style.display = "block"
    }else{
   	 document.getElementById("fromAccountDiv").style.display = "none";
   	}
   }
</script>

<script>
 function changeFuncToAccount(value) {
 	if(value == '-4'){
    		document.getElementById("toAccountDiv").style.display = "block"
    }else{
   	 document.getElementById("toAccountDiv").style.display = "none";
   	}
   }
</script>

	</body>
</html>