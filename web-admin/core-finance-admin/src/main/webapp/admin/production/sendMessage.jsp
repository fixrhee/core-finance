
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
						<input type="text" class="form-control" placeholder="Search for...">
							<span class="input-group-btn">
								<button class="btn btn-default" type="button">Go!</button>
							</span>
					</div>
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
						<div class="row">
							<div class="col-sm-3 mail_list_column">
								<button id="compose" class="btn btn-sm btn-success btn-block"
									type="button" data-placement="top" data-toggle="tooltip"
									data-original-title="Compose" onclick="location.href = 'composeMessage';">COMPOSE</button>

								<c:forEach var="messageListValue" items="${messageList}">
									${messageListValue}
								</c:forEach>

							</div>
							<!-- /MAIL LIST -->

							<!-- CONTENT MAIL -->
							${messageContent}
							<!-- /CONTENT MAIL -->

							<!-- compose -->
							<div class="col-sm-9 mail_view">
								<div class="compose-header">
									<h4>New Message</h4>
									<div class="ln_solid"></div>
								</div>

								<div class="compose-body">
									<form id="messageform" data-parsley-validate action="/admin/sendMessage"
										method="POST" modelAttribute="sendMessage" class="form-horizontal form-label-left">
										<div id="editor1" class="btn-toolbar editor">
											<input type="text" id="toMember" name="toMember" required="required" placeholder="To" style="width:760px;height:30px;padding:5px;border:none;">
										</div>
										<div id="editor2" class="btn-toolbar editor">
											<input type="text" id="subject" name="subject" required="required" placeholder="Subject" style="width:760px;height:30px;padding:5px;border:none;">
										</div>

										<div class="btn-toolbar editor" data-role="editor-toolbar"
											data-target="#editor">
											<div class="btn-group">
												<a class="btn dropdown-toggle" data-toggle="dropdown"
													title="Font">
													<i class="fa fa-font"></i>
													<b class="caret"></b>
												</a>
												<ul class="dropdown-menu">
												</ul>
											</div>

											<div class="btn-group">
												<a class="btn dropdown-toggle" data-toggle="dropdown"
													title="Font Size">
													<i class="fa fa-text-height"></i>&nbsp;
													<b class="caret"></b>
												</a>
												<ul class="dropdown-menu">
													<li>
														<a data-edit="fontSize 5">
															<p style="font-size:17px">Huge</p>
														</a>
													</li>
													<li>
														<a data-edit="fontSize 3">
															<p style="font-size:14px">Normal</p>
														</a>
													</li>
													<li>
														<a data-edit="fontSize 1">
															<p style="font-size:11px">Small</p>
														</a>
													</li>
												</ul>
											</div>

											<div class="btn-group">
												<a class="btn" data-edit="bold" title="Bold (Ctrl/Cmd+B)">
													<i class="fa fa-bold"></i>
												</a>
												<a class="btn" data-edit="italic" title="Italic (Ctrl/Cmd+I)">
													<i class="fa fa-italic"></i>
												</a>
												<a class="btn" data-edit="strikethrough" title="Strikethrough">
													<i class="fa fa-strikethrough"></i>
												</a>
												<a class="btn" data-edit="underline" title="Underline (Ctrl/Cmd+U)">
													<i class="fa fa-underline"></i>
												</a>
											</div>

											<div class="btn-group">
												<a class="btn" data-edit="insertunorderedlist" title="Bullet list">
													<i class="fa fa-list-ul"></i>
												</a>
												<a class="btn" data-edit="insertorderedlist" title="Number list">
													<i class="fa fa-list-ol"></i>
												</a>
												<a class="btn" data-edit="outdent" title="Reduce indent (Shift+Tab)">
													<i class="fa fa-dedent"></i>
												</a>
												<a class="btn" data-edit="indent" title="Indent (Tab)">
													<i class="fa fa-indent"></i>
												</a>
											</div>

											<div class="btn-group">
												<a class="btn" data-edit="justifyleft" title="Align Left (Ctrl/Cmd+L)">
													<i class="fa fa-align-left"></i>
												</a>
												<a class="btn" data-edit="justifycenter" title="Center (Ctrl/Cmd+E)">
													<i class="fa fa-align-center"></i>
												</a>
												<a class="btn" data-edit="justifyright" title="Align Right (Ctrl/Cmd+R)">
													<i class="fa fa-align-right"></i>
												</a>
												<a class="btn" data-edit="justifyfull" title="Justify (Ctrl/Cmd+J)">
													<i class="fa fa-align-justify"></i>
												</a>
											</div>

											<div class="btn-group">
												<a class="btn dropdown-toggle" data-toggle="dropdown"
													title="Hyperlink">
													<i class="fa fa-link"></i>
												</a>
												<div class="dropdown-menu input-append">
													<input class="span2" placeholder="URL" type="text"
														data-edit="createLink" />
													<button class="btn" type="button">Add</button>
												</div>
												<a class="btn" data-edit="unlink" title="Remove Hyperlink">
													<i class="fa fa-cut"></i>
												</a>
											</div>

											<div class="btn-group">
												<a class="btn" data-edit="undo" title="Undo (Ctrl/Cmd+Z)">
													<i class="fa fa-undo"></i>
												</a>
												<a class="btn" data-edit="redo" title="Redo (Ctrl/Cmd+Y)">
													<i class="fa fa-repeat"></i>
												</a>
											</div>
										</div>
										<div id="editor" class="editor-wrapper"></div>
								</div>
								<div class="form-group">
									<input id="username" name="username" value="${member.username}"
										class="date-picker form-control col-md-7 col-xs-12" type="hidden">
									<input type="hidden" id="body" name="body" readonly required="required" class="form-control col-md-7 col-xs-12">
								</div>
								<div class="form-group">
									<button type="submit" class="btn btn-success">Send</button>
								</div>
							</div>
						</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- /page content -->

<%@include file="footer.jsp" %>

<script>
	$('button[type=submit]').click(function(){
    	$('#body').val($('#editor').html());
	});
</script>

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