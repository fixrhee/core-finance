<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="header.jsp" %>

        <!-- page content -->
        <div class="right_col" role="main">
          <div class="">
            <div class="page-title">
              <div class="title_left">
                <h3>Webservice</h3>
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
                    <h2>Edit Webservice</h2>
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
                    <form id="webserviceform" data-parsley-validate action="/admin/editWebserviceForm" method="POST" modelAttribute="createwebservice" class="form-horizontal form-label-left">
				      <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="name">Webservice Name <span class="required">*</span> :
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="name" name="name" required="required" value="${name}" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                      <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="username">Username <span class="required">*</span> :
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="username" name="username" required="required" value="${username}"class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                      <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="password">Password <span class="required">*</span> :
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="password" id="password" name="password" required="required" value="${password}" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                      <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="hash">Hash <span class="required">*</span> :
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="hash" name="hash" required="required" value="${hash}" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                      <div class="form-group">
                          <label class="control-label col-md-3 col-sm-3 col-xs-12">Enabled </label>
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
                          <label class="control-label col-md-3 col-sm-3 col-xs-12">PIN Enabled </label>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                           <div class="">
                       		 <label>
                       		 <c:if test="${secure}">
                              <input type="checkbox" id="secure" name="secure" class="js-switch" value="1" checked /> True
                              </c:if>
                       		 <c:if test="${secure == false}">
                              <input type="checkbox" id="secure" name="secure" class="js-switch" value="1" /> True
                              </c:if>
                            </label>
                            </div>
                    	 	</div>
                      </div>
                		  <div class="form-group">
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="hidden" id="id" name="id" required="required" value="${wsID}" class="form-control col-md-7 col-xs-12">
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
function switchChanger(){
}
</script>

	</body>
</html>