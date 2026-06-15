// my-erp-frontend/.eslintrc.js

module.exports = {
  root: true,
  env: {
    browser: true,
    node: true,
    es2021: true
  },
  parser: "vue-eslint-parser",           // 用于解析 .vue 单文件
  parserOptions: {
    parser: "@babel/eslint-parser",      // 解析 <script> 里的 ES6+ 代码
    requireConfigFile: false,
    ecmaVersion: 2021,
    sourceType: "module",
    ecmaFeatures: {
      jsx: false
    }
  },
  extends: [
    "eslint:recommended",
    "plugin:vue/vue3-essential"
    // 如果要更严格，请切换为 vue3-strongly-recommended 或 vue3-recommended
    // "plugin:vue/vue3-strongly-recommended",
    // "plugin:vue/vue3-recommended",
    // 如果结合 Prettier，可添加：
    // "plugin:prettier/recommended",
    // "prettier"
  ],
  plugins: [
    "vue"
    // 如果结合 Prettier，可添加 "prettier"
  ],
  rules: {
    // 常见定制
    "no-console": process.env.NODE_ENV === "production" ? "warn" : "off",
    "no-debugger": process.env.NODE_ENV === "production" ? "warn" : "off",

    // 允许单文件组件名称为单个单词
    "vue/multi-word-component-names": "off",

    // 关闭对 <template> 中 Self-closing 的强制
    "vue/html-self-closing": "off",

    // 允许以 "_" 开头的函数参数未使用，方便写 actions(_, payload)
    "no-unused-vars": ["warn", { "argsIgnorePattern": "^_" }]
  }
};
