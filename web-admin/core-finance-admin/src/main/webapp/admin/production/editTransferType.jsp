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
                    <h2>Edit Transfer Type</h2>
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
                    <form id="trfTypeform" data-parsley-validate action="/admin/editTransferTypeForm" method="POST" modelAttribute="editTransferTypeModel" class="form-horizontal form-label-left">
					   <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="name">Transfer Type Name <span class="required">*</span> :
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="name" name="name" required="required" value="${name}" class="form-control col-md-7 col-xs-12">
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
		                <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">From Account :</label>
		                <div class="col-md-6 col-sm-6 col-xs-12">
		                  <select name="fromAccountName" class="select2_single form-control" tabindex="-1">
		               			<option name="fromAccountName" id="fromAccountName" value="${fromAccount}">${fromAccount}</option>
						   </select>
		                </div>
		              </div>
		              
		               <div class="form-group">
		                <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">To Account :</label>
		                <div class="col-md-6 col-sm-6 col-xs-12">
		                  <select name="toAccountName" class="select2_single form-control" tabindex="-1">
		               			<option name="toAccountName" id="toAccountName" value="${toAccount}">${toAccount}</option>
						   </select>
		                </div>
		              </div>
		              
		              <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="minAmount">Minimal Amount <span class="required">*</span> :
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="minAmount" name="minAmount" value="${minAmount}" required="required" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                      
                       <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="maxAmount">Maximum Amount <span class="required">*</span> :
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="maxAmount" name="maxAmount" value="${maxAmount}" required="required" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                     <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="otpThreshold">OTP Threshold <span class="required">*</span> :
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="otpThreshold" name="otpThreshold" value="${otpThreshold}" required="required" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                       <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="maxCount">Maximum Transaction / day <span class="required">*</span> :
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="maxCount" name="maxCount" required="required" value="${maxCount}" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                      
                      <div class="form-group">
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="hidden" id="id" name="id" value="${id}" required="required" class="form-control col-md-7 col-xs-12">
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
    $('#myDatepicker').datetimepicker({
   		minDate: moment().add('days', 1),
   		maxDate: moment().add('days', 90),
        ignoreReadonly: true,
        allowInputToggle: true
    });
</script>

	</body>
</html>