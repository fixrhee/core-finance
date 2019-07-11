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
					</div>
				</div>
			</div>
		</div>

		<div class="clearfix"></div>

		<div class="row">
			<div class="col-md-12 col-sm-12 col-xs-12">
				<div class="x_panel">
					<div class="x_title">
						<h2>
							</small>
								Broker Detail
							</small>
						</h2>
						<ul class="nav navbar-right panel_toolbox">
							<li>
								<a class="collapse-link">
									<i class="fa fa-chevron-up"></i>
								</a>
							</li>
							<li class="dropdown">
								<a href="#" class="dropdown-toggle" data-toggle="dropdown"
									role="button" aria-expanded="false">
									<i class="fa fa-wrench"></i>
								</a>
								<ul class="dropdown-menu" role="menu">
									<li>
										<a href="#">Settings 1</a>
									</li>
								</ul>
							</li>
							<li>
								<a class="close-link">
									<i class="fa fa-close"></i>
								</a>
							</li>
						</ul>
						<div class="clearfix"></div>
					</div>

					<div class="x_content">
						<table id="detailGroup" class="table table-striped">
							<tbody>
								<tr>
									<td width="150">ID </td>
									<td width="150"> : </td>
									<td align="right">${id}</td>
								</tr>
								<tr>
									<td width="150">Fee ID </td>
									<td width="150"> : </td>
									<td align="right">${feeID}</td>
								</tr>
								<tr>
									<td width="150">Name</td>
									<td width="150"> : </td>
									<td align="right">${name}</td>
								</tr>
								<tr>
									<td width="150">Description</td>
									<td width="150"> : </td>
									<td align="right">${description}</td>
								</tr>
								<tr>
									<td width="150">From Member </td>
									<td width="150"> : </td>
									<td align="right">${fromMember}</td>
								</tr>
								<tr>
									<td width="150">From Account </td>
									<td width="150"> : </td>
									<td align="right">${fromAccount}</td>
								</tr>
								<tr>
									<td width="150">To Member </td>
									<td width="150"> : </td>
									<td align="right">${toMember}</td>
								</tr>
								<tr>
									<td width="150">To Account </td>
									<td width="150"> : </td>
									<td align="right">${toAccount}</td>
								</tr>
								<tr>
									<td width="150">Fixed Amount </td>
									<td width="150"> : </td>
									<td align="right">${fixedAmount}</td>
								</tr>
								<tr>
									<td width="150">Percentage </td>
									<td width="150"> : </td>
									<td align="right">${percentageValue}</td>
								</tr>
								<tr>
									<td width="150">Deduct All Fee </td>
									<td width="150"> : </td>
									<td align="right">${fn:toUpperCase(deductAllFee)}</td>
								</tr>
								<tr>
									<td width="150">Enabled </td>
									<td width="150"> : </td>
									<td align="right">${fn:toUpperCase(enabled)}</td>
								</tr>
							</tbody>
						</table>
						<hr />
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
