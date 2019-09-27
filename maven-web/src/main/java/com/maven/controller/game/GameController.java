package com.maven.controller.game;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.maven.common.request.ParamUtils;
import com.maven.common.request.ResponseUtils;
import com.maven.controller.BaseController;
import com.maven.model.game.Game;

/**
 * 游戏Controller
 * 
 * @author chenjian
 * @createDate 2019-08-14
 */
@Controller
@RequestMapping(value = "/game")
public class GameController extends BaseController {

	/**
	 * 获取游戏信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "single", method = RequestMethod.GET)
	@ResponseBody
	public Object single(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String gameId = ParamUtils.getStringDefault(request, "gameId", "");

			Game game = baseService.findById(Game.class, gameId);
			return ResponseUtils.writeSuccess(game, true);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtils.writeFail();
		}
	}
}