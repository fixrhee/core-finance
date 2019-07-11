<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="header.jsp" %>

         <!-- page content -->
        <div class="right_col" role="main">
          <div class="">

            <div class="page-title">
              <div class="title_left">
                <h3>Message</h3>
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
              <div class="col-md-12">
                <div class="x_panel">
                  <div class="x_title">
                    <h2>Inbox</h2>
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
                    <div class="row">
                   <!-- /MAIL LIST -->
                      <div class="col-sm-3 mail_list_column btn-toolbar">
                      	<c:choose>
						 <c:when test="${member.groupName == 'ADMIN'}">
                        	<button id="compose" class="btn btn-sm btn-success btn-block" type="button" data-placement="top" data-toggle="tooltip" data-original-title="Compose" onclick="location.href = 'composeMessage';">COMPOSE</button>
                  		 </c:when>
                  <c:when test="${member.groupName == 'PARTNER'}">
                          <button id="compose" class="btn btn-sm btn-success btn-block" type="button" data-placement="top" data-toggle="tooltip" data-original-title="Compose" onclick="location.href = 'composeMessage';">COMPOSE</button>
                  </c:when>    
						 <c:otherwise>
							 <button disabled id="compose" class="btn btn-sm btn-success btn-block" type="button" data-placement="top" data-toggle="tooltip" data-original-title="Compose">COMPOSE</button>
						 </c:otherwise>
						</c:choose>
              <c:forEach var="messageListValue" items="${messageList}">
						  ${messageListValue}
				    	</c:forEach>
                                    
                      </div>
                      <!-- /MAIL LIST -->


                      <!-- CONTENT MAIL -->
						${messageContent}
                      <!-- /CONTENT MAIL -->
                      
                      
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- /page content -->

	<%@include file="footer.jsp" %>
	<c:if test="${not empty fn:trim(notification)}">
	<script type="text/javascript">
		$(function(){
	 	 new PNotify({
	        title: '${title}',
	        text: '${message}',
	        type: '${notification}',
	        styling: 'bootstrap3'
	        });
		});
		</script>
	</c:if>
	</body>
</html>