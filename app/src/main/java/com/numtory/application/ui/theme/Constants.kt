package com.numtory.application.ui.theme


const val REFRESH_TIMER = 130
const val PERCENT = 0.1
const val DEFAULT_TOKEN =  "USDT"
const val GOLD =  "GOLD"
const val SILVER =  "SILVER"

const val CHART_SCRIPT = """
<div id="bitycle-ac-widget" style="width: 100%; height: 100%"></div>
<script type="text/javascript" src="https://widget.bitycle.com/static/script/v1/script.js" async>
{
  "id": "bitycle-ac-widget",
  "theme": "bitycle",
  "type": "ac",
  "locale": "fa",
  "mode": "light",
  "style": "tradingview",
  "datafeed_type": "general",
  "symbol": "{symbol_hear}",
  "source_priority": [],
  "interval": "1D",
  "disabled_features": [
    "side_toolbar_in_fullscreen_mode",
    "show_right_widgets_panel_by_default",
    "fix_left_edge",
    "left_toolbar",
    "hide_left_toolbar_by_default",
    "reset_chart_on_return",
    "show_exchange",
    "show_interval_dialog_on_key_press",
    "header_symbol_search",
    "header_in_fullscreen_mode",
    "header_chart_type",
    "header_settings",
    "header_indicators",
    "header_compare",
    "header_undo_redo",
    "header_saveload",
    "header_quick_search"
  ],
  "enabled_features": [],
  "calendar_type": "shamsi",
  "chart_style": "Candle"
}
</script>
 """