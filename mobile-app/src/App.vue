<script>
	export default { onLaunch: function() { console.log('App Launch') } }
</script>
<style>
	/* 适老化全局样式 */
	page { background-color: #F5F7FA; font-family: sans-serif; }
    .container { padding: 20px; }
    .card { background: white; border-radius: 16px; padding: 24px; margin-bottom: 20px; box-shadow: 0 4px 12px rgba(0,0,0,0.05); }
    .title-lg { font-size: 24px; font-weight: 900; color: #333; margin-bottom: 10px; }
    .text-md { font-size: 18px; color: #666; line-height: 1.6; }
    .btn { font-size: 20px; font-weight: bold; height: 56px; line-height: 56px; border-radius: 28px; }
    .btn-primary { background: #FF6B00; color: white; } /* 暖橙色，适合老年人 */
    .btn-blue { background: #1890FF; color: white; }
</style>