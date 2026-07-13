var adminToken='';
var wcat=1;

function doLogin2(){
  var u=document.getElementById('luInput').value.trim();
  var p=document.getElementById('lpInput').value.trim();
  var m=document.getElementById('loginMsg');
  if(!u||!p){m.textContent='请输入账号和密码';return;}
  m.textContent='登录中...';
  fetch('/api/admin/login',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({username:u,password:p})})
  .then(function(r){return r.json()}).then(function(r){
    if(r.code===200){adminToken=r.data;document.getElementById('loginBox').style.display='none';document.getElementById('adminBox').style.display='block';}
    else m.textContent=r.msg||'登录失败';
  }).catch(function(e){m.textContent='网络错误';});
}

function doLogout(){adminToken='';document.getElementById('loginBox').style.display='block';document.getElementById('adminBox').style.display='none';}
function copyText(t){
  // 兼容所有浏览器的复制方法
  var ta=document.createElement('textarea');
  ta.value=t;ta.style.position='fixed';ta.style.left='-9999px';
  document.body.appendChild(ta);ta.select();
  try{document.execCommand('copy');alert('已复制: '+t);}catch(e){alert('复制失败，请手动复制:\n'+t);}
  document.body.removeChild(ta);
}
function wtOn(n){document.getElementById('wt1').className='';document.getElementById('wt2').className='';document.getElementById('wt3').className='';document.getElementById('wt'+n).className='on';}

function switchPanel(name){
  document.querySelectorAll('.panel-content').forEach(function(el){el.style.display='none';});
  var p=document.getElementById('panel-'+name);
  if(p)p.style.display='block';
  if(name==='words')loadWords();
  if(name==='apology')loadApology();
  if(name==='stories')loadStories();
  if(name==='benefits'){loadGoods();loadExchanges();}
  if(name==='truths')loadTruths();
  if(name==='chem')loadChem();
  if(name==='cards'){loadCards();loadCardSelect();}
  if(name==='annis')loadAnnis();
  if(name==='surprise')loadSurprises();
  if(name==='config')loadConfig();
  if(name==='records'){loadMoods();loadMsgs();}
}

function uploadFile(input,prefix,type){
  type=type||'voice';
  var f=input.files[0];if(!f)return;
  var msg=document.getElementById(prefix+'_msg');
  var url=document.getElementById(prefix+'_url');
  msg.style.display='block';msg.style.color='#9B8E9B';url.textContent='上传中...';
  var fd=new FormData();fd.append('file',f);
  var endpoint=type==='image'?'/api/admin/upload/material':'/api/admin/upload/voice';
  fetch(endpoint,{method:'POST',headers:{'Authorization':'Bearer '+adminToken},body:fd})
  .then(function(r){return r.json()}).then(function(r){
    if(r.code===200){
      msg.style.color='#E91E63';url.textContent=r.data.url;
      alert('上传成功！\nURL: '+r.data.url+'\n\n已自动填入下方URL框');
      if(prefix==='gimg'){var imgInput=document.getElementById('ngImg');if(imgInput)imgInput.value=r.data.url;}
      if(prefix==='wvu'){var wvInput=document.getElementById('adminNwv');if(wvInput)wvInput.value=r.data.url;}
    }else{msg.style.color='#f44336';url.textContent='失败: '+r.msg;alert('上传失败: '+r.msg);}
  }).catch(function(e){msg.style.color='#f44336';url.textContent='网络错误';alert('网络错误');});
}

// === WORDS ===
function loadWords(){
  fetch('/api/admin/comfort-words?category='+wcat+'&size=50',{headers:{'Authorization':'Bearer '+adminToken}})
  .then(function(r){return r.json()}).then(function(r){
    if(r.code!==200)return;
    document.getElementById('wordCount').textContent=(r.data.records||[]).length;
    var h='';
    (r.data.records||[]).forEach(function(w){
      h+='<div class="li"><div class="c">'+(w.content||'').substring(0,50)+'<br><span class="tag '+(w.status==1?'ton':'toff')+'">'+(w.status==1?'启用':'停用')+'</span>'+(w.voiceUrl?' <span style="color:#E91E63">🎙️</span>':'')+'</div><div class="a"><button class="btn bo bs2" onclick="toggleWord('+w.id+')">'+(w.status==1?'停用':'启用')+'</button><button class="btn br bs2" onclick="delWord('+w.id+')">删</button></div></div>';
    });
    document.getElementById('wordList').innerHTML=h||'<p style="text-align:center;color:#9B8E9B;padding:20px">暂无话术</p>';
  });
}
function addWord(){
  var c=document.getElementById('adminNw').value.trim();if(!c)return;
  var v=document.getElementById('adminNwv').value.trim();
  fetch('/api/admin/comfort-word',{method:'POST',headers:{'Content-Type':'application/json','Authorization':'Bearer '+adminToken},body:JSON.stringify({category:wcat,content:c,voiceUrl:v||null,status:1})})
  .then(function(r){return r.json()}).then(function(r){
    if(r.code===200){document.getElementById('adminNw').value='';document.getElementById('adminNwv').value='';loadWords();}
  });
}
function toggleWord(id){fetch('/api/admin/comfort-word/'+id+'/status',{method:'PUT',headers:{'Authorization':'Bearer '+adminToken}}).then(function(){loadWords();});}
function delWord(id){if(confirm('确认删除？')){fetch('/api/admin/comfort-word/'+id,{method:'DELETE',headers:{'Authorization':'Bearer '+adminToken}}).then(function(){loadWords();});}}

// === APOLOGY ===
function loadApology(){
  fetch('/api/admin/apology-letters',{headers:{'Authorization':'Bearer '+adminToken}}).then(function(r){return r.json()}).then(function(r){
    if(r.code===200&&r.data.records&&r.data.records.length>0){
      var a=r.data.records[0];
      document.getElementById('apTitle').value=a.title||'';document.getElementById('apContent').value=a.content||'';document.getElementById('apVoiceUrl').value=a.voiceUrl||'';
    }
  });
}
function saveApology(){
  var t=document.getElementById('apTitle').value.trim();var c=document.getElementById('apContent').value.trim();if(!t||!c)return;
  fetch('/api/admin/apology-letter',{method:'POST',headers:{'Content-Type':'application/json','Authorization':'Bearer '+adminToken},body:JSON.stringify({title:t,content:c,voiceUrl:document.getElementById('apVoiceUrl').value.trim()||null,status:1})})
  .then(function(){alert('已保存！');});
}

// === STORIES ===
function loadStories(){
  fetch('/api/admin/stories?size=200',{headers:{'Authorization':'Bearer '+adminToken}}).then(function(r){return r.json()}).then(function(r){
    if(r.code!==200)return;
    var h='';
    var sel=document.getElementById('bindStorySelect');
    if(sel)sel.innerHTML='<option value="0">-- 选择故事 --</option>';
    (r.data.records||[]).forEach(function(s){
      h+='<div class="li"><div class="c"><b>'+s.title+'</b> <span class="tag '+(s.source==1?'toff':'ton')+'">'+(s.source==1?'内置':'自定义')+'</span>'+(s.voiceUrl?' <span style="color:#E91E63">🎙️已录音</span>':'')+'</div><div class="a"><button class="btn br bs2" onclick="delStory('+s.id+')">删</button></div></div>';
      if(sel)sel.innerHTML+='<option value="'+s.id+'">'+s.title+(s.voiceUrl?' 🎙️':'')+'</option>';
    });
    document.getElementById('storyList').innerHTML=h||'<p style="text-align:center;color:#9B8E9B;padding:20px">暂无故事</p>';
  });
}

function bindVoiceToStory(){
  var storyId=document.getElementById('bindStorySelect').value;
  var voiceUrl=document.getElementById('bindVoiceInput').value.trim();
  if(!storyId||storyId==='0'){alert('请先选择一篇故事');return;}
  if(!voiceUrl){alert('请粘贴录音URL');return;}
  fetch('/api/admin/story/'+storyId,{method:'PUT',headers:{'Content-Type':'application/json','Authorization':'Bearer '+adminToken},body:JSON.stringify({id:parseInt(storyId),voiceUrl:voiceUrl})})
  .then(function(r){return r.json()}).then(function(r){
    if(r.code===200){alert('录音已绑定！');document.getElementById('bindVoiceInput').value='';loadStories();}
    else alert(r.msg||'绑定失败');
  });
}
function addStory(){
  var t=document.getElementById('nsTitle').value.trim();if(!t)return;
  var c=document.getElementById('nsContent').value.trim();
  fetch('/api/admin/story',{method:'POST',headers:{'Content-Type':'application/json','Authorization':'Bearer '+adminToken},body:JSON.stringify({title:t,content:c,source:2,status:1})})
  .then(function(r){return r.json()}).then(function(r){
    if(r.code===200){document.getElementById('nsTitle').value='';document.getElementById('nsContent').value='';loadStories();}
  });
}
function delStory(id){if(confirm('确认删除？')){fetch('/api/admin/story/'+id,{method:'DELETE',headers:{'Authorization':'Bearer '+adminToken}}).then(function(){loadStories();});}}

// === GOODS ===
function loadGoods(){
  fetch('/api/admin/benefits?size=50',{headers:{'Authorization':'Bearer '+adminToken}}).then(function(r){return r.json()}).then(function(r){
    if(r.code!==200)return;
    var h='';
    (r.data.records||[]).forEach(function(g){h+='<div class="li"><div class="c"><b>'+g.name+'</b> 💎'+g.points+' <span class="tag '+(g.status==1?'ton':'toff')+'">'+(g.status==1?'上架':'下架')+'</span></div><div class="a"><button class="btn bo bs2" onclick="toggleGoods('+g.id+')">'+(g.status==1?'下架':'上架')+'</button></div></div>';});
    document.getElementById('goodsList').innerHTML=h||'<p style="text-align:center;color:#9B8E9B;padding:20px">暂无商品</p>';
  });
}
function addGoods(){
  var n=document.getElementById('ngName').value.trim();var pts=parseInt(document.getElementById('ngPoints').value)||0;if(!n||!pts)return;
  fetch('/api/admin/benefit',{method:'POST',headers:{'Content-Type':'application/json','Authorization':'Bearer '+adminToken},body:JSON.stringify({name:n,points:pts,description:document.getElementById('ngDesc').value.trim(),imageUrl:document.getElementById('ngImg').value.trim(),status:1})})
  .then(function(r){return r.json()}).then(function(r){
    if(r.code===200){document.getElementById('ngName').value='';document.getElementById('ngPoints').value='';document.getElementById('ngDesc').value='';document.getElementById('ngImg').value='';loadGoods();}
  });
}
function toggleGoods(id){fetch('/api/admin/benefit/'+id+'/status',{method:'PUT',headers:{'Authorization':'Bearer '+adminToken}}).then(function(){loadGoods();});}
function loadExchanges(){
  fetch('/api/admin/exchange-records?size=50',{headers:{'Authorization':'Bearer '+adminToken}}).then(function(r){return r.json()}).then(function(r){
    if(r.code!==200)return;
    var h='';
    (r.data.records||[]).forEach(function(e){h+='<div class="li"><div class="c">'+e.goodsName+' 💎'+e.points+' <span style="color:'+(e.status==1?'#4caf50':'#FF9800')+'">'+(e.status==1?'已兑现':'待兑现')+'</span></div><div class="a">'+(e.status==0?'<button class="btn bp bs2" onclick="fulfill('+e.id+')">标记兑现</button>':'')+'</div></div>';});
    document.getElementById('exList').innerHTML=h||'<p style="text-align:center;color:#9B8E9B;padding:20px">暂无兑换记录</p>';
  });
}
function fulfill(id){fetch('/api/admin/exchange-record/'+id+'/fulfill',{method:'PUT',headers:{'Authorization':'Bearer '+adminToken}}).then(function(){loadExchanges();});}

// === CARDS ===
function loadCards(){
  fetch('/api/admin/exemption-cards?size=50',{headers:{'Authorization':'Bearer '+adminToken}}).then(function(r){return r.json()}).then(function(r){
    if(r.code!==200)return;
    var h='';
    (r.data.records||[]).forEach(function(c){h+='<div class="li"><div class="c"><b>'+c.name+'</b> '+(c.description||'')+' | '+(c.validDays||'永久')+'天</div><div class="a"><button class="btn br bs2" onclick="delCard('+c.id+')">删</button></div></div>';});
    document.getElementById('cardList').innerHTML=h||'<p style="text-align:center;color:#9B8E9B;padding:20px">暂无卡类型</p>';
  });
}
function loadCardSelect(){
  fetch('/api/admin/exemption-cards?size=50',{headers:{'Authorization':'Bearer '+adminToken}}).then(function(r){return r.json()}).then(function(r){
    if(r.code!==200)return;
    var s=document.getElementById('gci');s.innerHTML='<option value="0">选卡类型</option>';
    (r.data.records||[]).forEach(function(c){s.innerHTML+='<option value="'+c.id+'">'+c.name+'</option>';});
  });
}
function addCardDef(){
  var n=document.getElementById('ncName').value.trim();if(!n)return;
  fetch('/api/admin/exemption-card',{method:'POST',headers:{'Content-Type':'application/json','Authorization':'Bearer '+adminToken},body:JSON.stringify({name:n,cardStyle:parseInt(document.getElementById('ncStyle').value)||1,validDays:document.getElementById('ncDays').value?parseInt(document.getElementById('ncDays').value):null})})
  .then(function(r){return r.json()}).then(function(r){if(r.code===200){document.getElementById('ncName').value='';loadCards();loadCardSelect();}});
}
function delCard(id){if(confirm('确认删除？')){fetch('/api/admin/exemption-card/'+id,{method:'DELETE',headers:{'Authorization':'Bearer '+adminToken}}).then(function(){loadCards();loadCardSelect();});}}
function grantCard(){
  var cid=parseInt(document.getElementById('gci').value)||0;if(!cid)return;
  fetch('/api/admin/exemption-card/grant',{method:'POST',headers:{'Content-Type':'application/json','Authorization':'Bearer '+adminToken},body:JSON.stringify({cardDefId:cid,quantity:parseInt(document.getElementById('gq').value)||1})})
  .then(function(){alert('已授予！');});
}

// === ANNIVERSARIES ===
function loadAnnis(){
  fetch('/api/admin/anniversaries',{headers:{'Authorization':'Bearer '+adminToken}}).then(function(r){return r.json()}).then(function(r){
    if(r.code!==200)return;
    var h='';
    (r.data||[]).forEach(function(a){h+='<div class="li"><div class="c">'+(a.icon||'💝')+' <b>'+a.name+'</b> '+(a.anniversaryDate||'').substring(0,10)+' '+(a.repeatType==1?'每年':'单次')+'</div><div class="a"><button class="btn br bs2" onclick="delAnni('+a.id+')">删</button></div></div>';});
    document.getElementById('anniList').innerHTML=h||'<p style="text-align:center;color:#9B8E9B;padding:20px">暂无纪念日</p>';
  });
}
function addAnni(){
  var n=document.getElementById('naName').value.trim();var d=document.getElementById('naDate').value;if(!n||!d)return;
  fetch('/api/admin/anniversary',{method:'POST',headers:{'Content-Type':'application/json','Authorization':'Bearer '+adminToken},body:JSON.stringify({name:n,anniversaryDate:d,repeatType:parseInt(document.getElementById('naRt').value)||1,icon:document.getElementById('naIcon').value.trim()||'💝'})})
  .then(function(r){return r.json()}).then(function(r){if(r.code===200){document.getElementById('naName').value='';document.getElementById('naDate').value='';loadAnnis();}});
}
function delAnni(id){if(confirm('确认删除？')){fetch('/api/admin/anniversary/'+id,{method:'DELETE',headers:{'Authorization':'Bearer '+adminToken}}).then(function(){loadAnnis();});}}

// === CONFIG ===
function loadConfig(){
  fetch('/api/admin/config',{headers:{'Authorization':'Bearer '+adminToken}}).then(function(r){return r.json()}).then(function(r){
    if(r.code!==200)return;
    document.getElementById('cfgSlogan').value=r.data.page_slogan||'';document.getElementById('cfgGirl').value=r.data.girl_nickname||'';
    document.getElementById('cfgBoy').value=r.data.boy_nickname||'';document.getElementById('cfgLoveDate').value=r.data.love_start_date||'';
    document.getElementById('cfgPwd').value=r.data.access_password||'';document.getElementById('cfgColor').value=r.data.theme_color||'#FFB6C1';
  });
}
function saveConfig(){
  var d={page_slogan:document.getElementById('cfgSlogan').value,girl_nickname:document.getElementById('cfgGirl').value,boy_nickname:document.getElementById('cfgBoy').value,love_start_date:document.getElementById('cfgLoveDate').value,access_password:document.getElementById('cfgPwd').value,theme_color:document.getElementById('cfgColor').value};
  fetch('/api/admin/config',{method:'PUT',headers:{'Content-Type':'application/json','Authorization':'Bearer '+adminToken},body:JSON.stringify(d)})
  .then(function(r){return r.json()})
  .then(function(r){
    if(r.code===200){alert('✅ 配置已保存！');}
    else{alert('❌ 保存失败：'+ (r.msg||'未知错误'));}
  })
  .catch(function(){alert('❌ 网络错误，保存失败');});
}

// === TRUTHS ===
var truthCatFilter=0;
function ttOn(n){for(var i=0;i<4;i++){var el=document.getElementById('tt'+i);if(el)el.className='';}var el=document.getElementById('tt'+n);if(el)el.className='on';}
function loadTruths(){
  var url='/api/admin/truth-questions?size=200';
  if(truthCatFilter>0)url+='&category='+truthCatFilter;
  fetch(url,{headers:{'Authorization':'Bearer '+adminToken}}).then(function(r){return r.json()}).then(function(r){
    if(r.code!==200)return;
    var cats={1:'回忆',2:'期待',3:'日常'};
    var h='';
    (r.data.records||[]).forEach(function(t){h+='<div class="li"><div class="c"><span class="tag ton">'+(cats[t.category]||'')+'</span> '+(t.question||'').substring(0,50)+'</div><div class="a"><button class="btn br bs2" onclick="delTruth('+t.id+')">删</button></div></div>';});
    document.getElementById('truthList').innerHTML=h||'<p style="text-align:center;color:#9B8E9B;padding:20px">暂无问题</p>';
  });
}
function addTruth(){
  var q=document.getElementById('truthQ').value.trim();if(!q)return;
  var cat=parseInt(document.getElementById('truthCat').value)||1;
  fetch('/api/admin/truth-question',{method:'POST',headers:{'Content-Type':'application/json','Authorization':'Bearer '+adminToken},body:JSON.stringify({category:cat,question:q})})
  .then(function(r){return r.json()}).then(function(r){
    if(r.code===200){document.getElementById('truthQ').value='';loadTruths();}
    else alert(r.msg);
  });
}
function delTruth(id){if(confirm('确认删除？')){fetch('/api/admin/truth-question/'+id,{method:'DELETE',headers:{'Authorization':'Bearer '+adminToken}}).then(function(){loadTruths();});}}

// === CHEMISTRY ===
function loadChem(){
  fetch('/api/admin/chemistry-questions',{headers:{'Authorization':'Bearer '+adminToken}}).then(function(r){return r.json()}).then(function(r){
    if(r.code!==200)return;
    document.getElementById('chemCount').textContent=(r.data||[]).length;
    var h='';
    (r.data||[]).forEach(function(q,i){var opts=JSON.parse(q.opts||'[]');h+='<div class="li"><div class="c">'+(i+1)+'. '+q.question+'<br><span style="font-size:11px;color:#9B8E9B">选项:'+opts.join(',')+' | 答案:第'+(q.answer+1)+'个</span></div><div class="a"><button class="btn br bs2" onclick="delChem('+q.id+')">删</button></div></div>';});
    document.getElementById('chemList').innerHTML=h||'<p style="text-align:center;color:#9B8E9B;padding:20px">暂无题目</p>';
  });
}
function addChem(){
  var q=document.getElementById('chemQ').value.trim();if(!q)return;
  var o=document.getElementById('chemOpts').value.trim();if(!o)return;
  var a=parseInt(document.getElementById('chemAns').value)||0;
  var opts=JSON.stringify(o.split(/[,，]/).map(function(s){return s.trim()}).filter(Boolean));
  fetch('/api/admin/chemistry-question',{method:'POST',headers:{'Content-Type':'application/json','Authorization':'Bearer '+adminToken},body:JSON.stringify({question:q,opts:opts,answer:a})})
  .then(function(r){return r.json()}).then(function(r){
    if(r.code===200){document.getElementById('chemQ').value='';document.getElementById('chemOpts').value='';document.getElementById('chemAns').value='';loadChem();}
  });
}
function delChem(id){if(confirm('确认删除？')){fetch('/api/admin/chemistry-question/'+id,{method:'DELETE',headers:{'Authorization':'Bearer '+adminToken}}).then(function(){loadChem();});}}

// === SURPRISE ===
var surTypes={1:'💌情话',2:'🧧红包',3:'🏷️折扣',4:'🥚彩蛋'};
function loadSurprises(){
  fetch('/api/admin/daily-surprises',{headers:{'Authorization':'Bearer '+adminToken}}).then(function(r){return r.json()}).then(function(r){
    if(r.code!==200)return;
    var h='';
    (r.data.records||[]).forEach(function(s){h+='<div class="li"><div class="c"><span class="tag ton">'+(surTypes[s.type]||'')+'</span> '+s.content+(s.points?' 💎'+s.points:'')+'</div><div class="a"><button class="btn br bs2" onclick="delSur('+s.id+')">删</button></div></div>';});
    document.getElementById('surList').innerHTML=h||'<p style="text-align:center;color:#9B8E9B;padding:20px">暂无自定义惊喜</p>';
  });
}
function addSurprise(){
  var c=document.getElementById('surContent').value.trim();if(!c)return;
  var t=parseInt(document.getElementById('surType').value)||1;
  var p=parseInt(document.getElementById('surPoints').value)||0;
  fetch('/api/admin/daily-surprise',{method:'POST',headers:{'Content-Type':'application/json','Authorization':'Bearer '+adminToken},body:JSON.stringify({type:t,content:c,points:p})})
  .then(function(r){return r.json()}).then(function(r){
    if(r.code===200){document.getElementById('surContent').value='';document.getElementById('surPoints').value='';loadSurprises();}
  });
}
function delSur(id){if(confirm('确认删除？')){fetch('/api/admin/daily-surprise/'+id,{method:'DELETE',headers:{'Authorization':'Bearer '+adminToken}}).then(function(){loadSurprises();});}}

function delMsg(id){if(confirm('确认删除这条留言？')){fetch('/api/admin/message/'+id,{method:'DELETE',headers:{'Authorization':'Bearer '+adminToken}}).then(function(){loadMsgs();});}}
function delMood(id){if(confirm('确认删除这条情绪记录？')){fetch('/api/admin/mood-record/'+id,{method:'DELETE',headers:{'Authorization':'Bearer '+adminToken}}).then(function(){loadMoods();});}}

// === RECORDS ===
function loadMoods(){
  fetch('/api/admin/mood-records?size=20',{headers:{'Authorization':'Bearer '+adminToken}}).then(function(r){return r.json()}).then(function(r){
    if(r.code!==200)return;
    var h='';
    (r.data.records||[]).forEach(function(m){var t=m.createTime?new Date(m.createTime):null;var ts=t?t.getFullYear()+'-'+String(t.getMonth()+1).padStart(2,'0')+'-'+String(t.getDate()).padStart(2,'0')+' '+String(t.getHours()).padStart(2,'0')+':'+String(t.getMinutes()).padStart(2,'0'):'';h+='<div class="li"><div class="c">'+(m.moodText||'')+' '+(m.sceneText||'')+' <span style="font-size:11px;color:#9B8E9B">'+ts+'</span></div><div class="a"><button class="btn br bs2" onclick="delMood('+m.id+')">删</button></div></div>';});
    document.getElementById('moodList').innerHTML=h||'<p style="text-align:center;color:#9B8E9B;padding:20px">暂无记录</p>';
  });
}
function loadMsgs(){
  fetch('/api/admin/messages?size=30',{headers:{'Authorization':'Bearer '+adminToken}}).then(function(r){return r.json()}).then(function(r){
    if(r.code!==200)return;
    var h='';
    (r.data.records||[]).forEach(function(m){var t=m.createTime?new Date(m.createTime):null;var ts=t?t.getFullYear()+'-'+String(t.getMonth()+1).padStart(2,'0')+'-'+String(t.getDate()).padStart(2,'0')+' '+String(t.getHours()).padStart(2,'0')+':'+String(t.getMinutes()).padStart(2,'0'):'';h+='<div class="li"><div class="c"><b>'+(m.role==1?'男孩':'女孩')+'</b>: '+(m.content||'').substring(0,40)+'<br><span style="font-size:11px;color:#9B8E9B">'+ts+'</span></div><div class="a"><button class="btn br bs2" onclick="delMsg('+m.id+')">删</button></div></div>';});
    document.getElementById('msgList').innerHTML=h||'<p style="text-align:center;color:#9B8E9B;padding:20px">暂无留言</p>';
  });
}