module.exports = {
  apps: [{
    name: 'gateway',
    script: './gateway.js',
    instances: 6,
    max_memory_restart: '8G',
    env_production: {
           NODE_ENV: "production"
    }
  }]
}
