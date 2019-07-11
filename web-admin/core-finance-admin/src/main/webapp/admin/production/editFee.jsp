

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
                    <h2>Edit Fee</h2>
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
                    <form id="feeForm" data-parsley-validate action="/admin/editFeeForm" method="POST" modelAttribute="createFeeModel" class="form-horizontal form-label-left">
					  
					   <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="name">Fee Name <span class="required">*</span> :
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="name" name="name" value="${name}" required="required" class="form-control col-md-7 col-xs-12">
                        </div>
                       </div>
                      
                       <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="description">Description <span class="required">*</span> :
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="description" name="description" value="${description}" required="required" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
              
                       <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12"> Deduct From </span> : 
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="fromMember" name="fromMember" value="${fromMemberName}" readonly class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
           			  
           			  <div id="fromAccountDiv" class="form-group">
		                <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name"> From Account :</font></label>
		                <div class="col-md-6 col-sm-6 col-xs-12">
		                   <input type="text" id="fromAccount" name="fromAccount" value="${fromAccountName}" readonly class="form-control col-md-7 col-xs-12">
                      </div>
		              </div>
		              
                       <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12">Credit to : 
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="toMember" name="toMember" value="${toMemberName}" readonly class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                    
           			  <div id="toAccountDiv" class="form-group">
		                <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">To Account : </label>
		                <div class="col-md-6 col-sm-6 col-xs-12">
		                   <input type="text" id="toAccount" name="toAccount" value="${toAccountName}" readonly class="form-control col-md-7 col-xs-12">
                      </div>
		              </div>
		             
			           <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="minAmount">Minimal Amount <span class="required">*</span> :
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="minAmount" name="minAmount" value="${initialRangeAmount}" required="required" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                      
                       <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="maxAmount">Maximum Amount <span class="required">*</span> :
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="maxAmount" name="maxAmount" value="${maximumRangeAmount}" required="required" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                      
		              <hr />
		              
		               <div class="form-group">
                          <label class="control-label col-md-3 col-sm-3 col-xs-12">Filter by Source Member </label>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                           <div class="">
                       		 <label>
                       		  <c:if test="${filterSource}">
                           		 <input type="checkbox" id="allowFromAllMember" name="allowFromAllMember" class="js-switch" value="1" /> True
                                </c:if>
                        		  <c:if test="${filterSource == false}">
                        		     <input type="checkbox" id="allowFromAllMember" name="allowFromAllMember" class="js-switch" value="1" checked /> True
                              </c:if>
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
                       		 <c:if test="${filterDestination}">
                           		 <input type="checkbox" id="allowToAllMember" name="allowToAllMember" class="js-switch" value="1" /> True
                                </c:if>
                        		  <c:if test="${filterDestination == false}">
                        		     <input type="checkbox" id="allowToAllMember" name="allowToAllMember" class="js-switch" value="1" checked /> True
                              </c:if>
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
                       		 <c:if test="${feePeriode}">
                              <input type="checkbox" id="periode" name="periode" class="js-switch" value="1" onchange='handleChange(this);' checked /> True
                                </c:if>
                        		  <c:if test="${feePeriode == false}">
                              <input type="checkbox" id="periode" name="periode" class="js-switch" value="1"  onchange='handleChange(this);' /> True
                              </c:if>
                       		 
                            </label>
                            </div>
                    		 </div>
                    	  </div>
                    	  
		               <div class="form-group">
                       <label class="control-label col-md-3 col-sm-3 col-xs-12" for="expired">Start Date <span class="required">*</span>
                        </label>
                         <div class="col-md-6 col-sm-6 col-xs-12">
                          <div class='input-group date' id='startDatepicker'>
                            <input type='text' id="startDate" name="startDate" value="${startDate}" class="form-control" readonly="readonly" />
                            <span class="input-group-addon">
                               <span class="glyphicon glyphicon-calendar"></span>
                            </span>
                        </div>
                       </div>
                    	</div>
                    	
                    	<div class="form-group">
                       <label class="control-label col-md-3 col-sm-3 col-xs-12" for="expired">End Date <span class="required">*</span>
                        </label>
                         <div class="col-md-6 col-sm-6 col-xs-12">
                          <div class='input-group date' id='endDatepicker'>
                            <input type='text' id="endDate" name="endDate" value="${endDate}" class="form-control" readonly="readonly" />
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
                       		  <c:if test="${deductAmount}">
                           		 <input type="checkbox" id="deductAmount" name="deductAmount" class="js-switch" value="1" checked /> True
                                </c:if>
                        		  <c:if test="${deductAmount == false}">
                        		     <input type="checkbox" id="deductAmount" name="deductAmount" class="js-switch" value="1" /> True
                              </c:if> 
                            </label>
                            </div>
                    		 </div>
                    	  </div>
                      
                     <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="fixedAmount">Fixed Amount <span class="required">*</span> :
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="fixedAmount" name="fixedAmount" required="required" value="${fixedAmount}" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                      
                       <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="percentage">Percentage <span class="required">*</span> :
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="percentage" name="percentage" required="required" value="${percentageValue}" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                      
                      <hr />
                      
                       <div class="form-group">
                          <label class="control-label col-md-3 col-sm-3 col-xs-12">Priority </label>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                           <div class="">
                       		 <label>
                       		 <c:if test="${priority}">
                           		 <input type="checkbox" id="priority" name="priority" class="js-switch" value="1" checked /> True
                                </c:if>
                        		  <c:if test="${priority == false}">
                        		     <input type="checkbox" id="priority" name="priority" class="js-switch" value="1" /> True
                              </c:if> 
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
                       		 <c:if test="${enabled}">
                           		 <input type="checkbox" id="enabled" name="enabled" class="js-switch" value="1" checked /> True
                                </c:if>
                        		  <c:if test="${enabled == false}">
                        		     <input type="checkbox" id="enabled" name="enabled" class="js-switch" value="1" /> True
                              </c:if> 
                            </label>
                            </div>
                    		 </div>
                    	  </div>
            
                      
                      <div class="form-group">
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="hidden" id="transferTypeID" name="transferTypeID" required="required" value="${transferTypeID}" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                      
                      <div class="form-group">
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="hidden" id="id" name="id" required="required" value="${feeID}" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                      
                      
                      <div class="ln_solid"></div>
                      <div class="form-group">
                        <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
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
        ignoreReadonly: true,
        allowInputToggle: true
    });
</script>
<script>
    $('#endDatepicker').datetimepicker({
        ignoreReadonly: true,
        allowInputToggle: true
    });
</script>

<script>
function handleChange(checkbox) {
  if(checkbox.checked == true){
    document.getElementById("startDate").value = "Please Select Date"
    document.getElementById("endDate").value = "Please Select Date"
  }else{
 	document.getElementById("startDate").value = ""
    document.getElementById("endDate").value = "";
    }
}
</script>

	</body>
</html>