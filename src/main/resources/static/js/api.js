/**
 * 情侣亲密度升温系统 - API 工具
 */
const API = {
  base: '/api',

  async get(url, params) {
    const qs = params ? '?' + new URLSearchParams(params).toString() : '';
    const res = await fetch(this.base + url + qs);
    return res.json();
  },

  async post(url, data) {
    const res = await fetch(this.base + url, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
    return res.json();
  },

  async put(url, data) {
    const res = await fetch(this.base + url, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
    return res.json();
  },

  async del(url) {
    const res = await fetch(this.base + url, { method: 'DELETE' });
    return res.json();
  },

  async upload(url, formData) {
    const res = await fetch(this.base + url, {
      method: 'POST',
      body: formData
    });
    return res.json();
  }
};

/**
 * Toast 提示
 */
function showToast(msg, duration = 2000) {
  const toast = document.createElement('div');
  toast.className = 'toast';
  toast.textContent = msg;
  document.body.appendChild(toast);
  setTimeout(() => {
    toast.style.opacity = '0';
    toast.style.transition = 'opacity 0.3s';
    setTimeout(() => toast.remove(), 300);
  }, duration);
}

/**
 * 爱心飘落特效
 */
function showHearts(count = 15) {
  const container = document.createElement('div');
  container.className = 'hearts-container';
  const hearts = ['💕', '💗', '💖', '💝', '💘', '✨', '🌸', '💐'];
  for (let i = 0; i < count; i++) {
    const heart = document.createElement('span');
    heart.className = 'heart-particle';
    heart.textContent = hearts[Math.floor(Math.random() * hearts.length)];
    heart.style.left = Math.random() * 100 + '%';
    heart.style.animationDelay = Math.random() * 2 + 's';
    heart.style.animationDuration = (2 + Math.random() * 3) + 's';
    heart.style.fontSize = (18 + Math.random() * 30) + 'px';
    container.appendChild(heart);
  }
  document.body.appendChild(container);
  setTimeout(() => container.remove(), 5000);
}

/**
 * 加载状态
 */
function showLoading(container) {
  container.innerHTML = '<div class="loading"><div class="loading-heart">💗</div></div>';
}

/**
 * 空状态
 */
function showEmpty(container, msg) {
  container.innerHTML = `
    <div class="empty-state">
      <div class="icon">💌</div>
      <p>${msg}</p>
    </div>`;
}

/**
 * 获取今日日期字符串
 */
function todayStr() {
  const d = new Date();
  return d.getFullYear() + '-' + String(d.getMonth() + 1).padStart(2, '0') + '-' + String(d.getDate()).padStart(2, '0');
}

/**
 * 加载顶部标语（所有页面自动调用）
 */
function loadTopSlogan() {
  var el = document.getElementById('topSlogan');
  if (!el) { console.log('[Slogan] 未找到 #topSlogan 元素'); return; }
  console.log('[Slogan] 开始加载标语...');
  fetch('/api/system/config/public')
    .then(function(r) { return r.json(); })
    .then(function(r) {
      console.log('[Slogan] API返回:', r);
      if (r.code === 200 && r.data && r.data.slogan) {
        console.log('[Slogan] 设置标语为:', r.data.slogan);
        el.textContent = r.data.slogan;
      } else {
        console.log('[Slogan] 数据格式异常');
      }
    })
    .catch(function(e) { console.log('[Slogan] 请求失败:', e); });
}

// 页面加载时自动执行
if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', loadTopSlogan);
} else {
  loadTopSlogan();
}

/**
 * 页面访问密码验证
 */
function getPassword() {
  return localStorage.getItem('cl_access') || '';
}
function setPassword(pwd) {
  localStorage.setItem('cl_access', pwd);
}
function clearPassword() {
  localStorage.removeItem('cl_access');
}
