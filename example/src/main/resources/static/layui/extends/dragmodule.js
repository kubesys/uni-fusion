layui.config({
	base: 'layui/plugins/'
});

layui.define(['jquery', 'input', 'grid', 'field', 'select','checkbox','date'], function(exports) {
	var $ = layui.jquery;
	var inputPlugin = layui.input;
	var gridPlugin = layui.grid;
	var fieldPlugin = layui.field;
	var selectPlugin = layui.select;
	var checkboxPlugin = layui.checkbox;
	var datePlugin = layui.date;
	var idx = 1;

	var factory = {
		pluginsObs: [],
		instance: function(elemName) {
			var plugin = null;
			switch (elemName) {
				case "input":
					plugin = inputPlugin.create();
					break;
				case "field":
					plugin = fieldPlugin.create();
					break;
				case "grid":
					plugin = gridPlugin.create();
					break;
				case "select":
					plugin = selectPlugin.create();
					break;
				case "checkbox":
					plugin = checkboxPlugin.create();
					break;
				case "date":
					plugin = datePlugin.create();
					break;
			}
			if (plugin == null) {
				return null;
			}
			var id = genId();
			var elem = plugin.createNode(id);
			// elem.setAttribute("id", id);
			elem.setAttribute("index", idx);
			elem.setAttribute("mode", elemName);


			var ctrlModes = elem.getElementsByTagName('input');
			for (i=0;i<ctrlModes.length;i++) {
				var ct = ctrlModes[i];
				// ct.id = elemName + '-' + id;
				ct.name = elemName + '-' + id;
			}
			activeClickBind(elem);
			activeControl(elem);
			this.pluginsObs.push({
				name: elemName,
				elem: elem
			})
			idx++;
			return elem;
		}
	};


	/**
	 * 生成唯一id
	 */
	function genId() {
		return (Math.random() * 10000000).toString(16).substr(0, 4) + '-' + (new Date()).getTime() + '-' + Math.random().toString()
			.substr(2, 5);
	}
	/**
	 * 阻止叠加点击事件 冒泡传播
	 * @param {Object} e
	 */
	function stopBubbling(e) {
		e = window.event || e;
		if (e.stopPropagation) {
			e.stopPropagation(); //阻止事件 冒泡传播
		} else {
			e.cancelBubble = true; //ie兼容
		}
	}

	var activeContainer = null;
	/**
	 * 给控件绑定点击激活框
	 * @param {Object} elem
	 */
	var activeClickBind = function(elem) {
		$(elem).click(function(event) {
			if (activeContainer.attr('id') != $(this).attr('id')) {
				activeControl(this);
			}
			stopBubbling(event);
		})
	}

	var activeControl = function(elem) {
		elem = $(elem);
		if (activeContainer != null) {
			activeContainer.children('.widget-view-drag').remove();
			activeContainer.children('.widget-view-action').remove();
			activeContainer.removeClass('widget-view-ctrl-action');
			activeContainer.addClass('ays-ctrl');
		}


		activeContainer = elem;
		var properties = $("#properties");
		properties.attr("src", "layui/plugins/" + elem.attr('mode') + ".html?new=" + Math.random());
		properties.load(function() {
			properties[0].contentWindow.setControl(elem);
		})


		activeContainer.addClass('widget-view-ctrl-action');
		activeContainer.removeClass('ays-ctrl');
		if (activeContainer.find(".widget-view-drag").length == 0) {
			var widgetViewDrag = $(
				'<div><i class="layui-icon layui-icon-addition" style="color:#FFFFFF;font-weight:600"></i></div>');
			widgetViewDrag.addClass('widget-view-drag');
			activeContainer.prepend(widgetViewDrag);
		}
		if (activeContainer.find(".widget-view-action").length == 0) {
			var widgetViewAction = $('<div></div>');
			widgetViewAction.addClass('widget-view-action');
			var widgetViewActionCopyBtn = $('<i class="layui-icon layui-icon-list" style="color:#FFFFFF;"></i>');
			widgetViewActionCopyBtn.click(function() {
				var copyNewElem = elem.clone(true);
				copyNewElem.id = genId();
				activeControl(copyNewElem);
				activeClickBind(copyNewElem);
				elem.parent().append(copyNewElem);
			})
			widgetViewAction.append(widgetViewActionCopyBtn);
			var widgetViewActionDeleteBtn = $('<i class="layui-icon layui-icon-delete" style="color:#FFFFFF;"></i>');
			widgetViewActionDeleteBtn.click(function() {
				elem.remove();
			})
			widgetViewAction.append(widgetViewActionDeleteBtn);
			activeContainer.prepend(widgetViewAction);
		}
	}



	var dragmodule = {
		genId: genId(),
		activeClickBind: activeClickBind,
		activeControl: activeControl,
		factory: factory
	}
	exports('dragmodule', dragmodule);
});
