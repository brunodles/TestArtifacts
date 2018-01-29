module.exports = {
  use: [
    '@neutrinojs/standardjs',
    [
      '@neutrinojs/react',
      {
        html: {
          title: 'report-analyzer'
        }
      }
    ],
    '@neutrinojs/jest'
  ]
};
