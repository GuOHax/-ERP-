// Front/src/common/utils.js
export function formatDate(dateStr, fmt = 'yyyy-MM-dd HH:mm:ss') {
  if (!dateStr) return '';
  const d = new Date(dateStr);
  const o = {
    'yyyy': d.getFullYear(),
    'MM': String(d.getMonth() + 1).padStart(2, '0'),
    'dd': String(d.getDate()).padStart(2, '0'),
    'HH': String(d.getHours()).padStart(2, '0'),
    'mm': String(d.getMinutes()).padStart(2, '0'),
    'ss': String(d.getSeconds()).padStart(2, '0')
  };
  let str = fmt;
  for (const k in o) {
    str = str.replace(k, o[k]);
  }
  return str;
}

export function formatDateTime(isoString) {
  if (!isoString) return '';
  const dt = new Date(isoString);
  const pad = (n) => (n < 10 ? '0' + n : n);
  return `${dt.getFullYear()}-${pad(dt.getMonth() + 1)}-${pad(dt.getDate())} ${pad(dt.getHours())}:${pad(dt.getMinutes())}:${pad(dt.getSeconds())}`;
}
